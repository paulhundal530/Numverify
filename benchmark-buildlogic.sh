#!/usr/bin/env bash
#
# Benchmarks the two ways this build can resolve its `numverify.*` convention plugins — as an
# included `build-logic` build, or as a jar published to mavenLocal — against each other in a
# single gradle-profiler run, so both series come off the same machine in the same thermal state.
#
#   ./benchmark-buildlogic.sh              # isolated Gradle user home (default)
#   ./benchmark-buildlogic.sh --real-home  # use ~/.gradle instead
#
# Anything after the recognised flags is passed straight through to gradle-profiler, so e.g.
# `./benchmark-buildlogic.sh --iterations 3` works for a quick sanity run.

set -euo pipefail

cd "$(dirname "$0")"

# gradle-profiler defaults to an isolated `gradle-user-home/` directory next to the project, which
# keeps runs reproducible but re-downloads every dependency the first time. `--real-home` points it
# at the actual Gradle home for numbers closer to what a developer sees day to day, at the cost of
# sharing daemons and caches with whatever else is running (the IDE, most likely).
GRADLE_USER_HOME_ARGS=()
if [[ "${1:-}" == "--real-home" ]]; then
    GRADLE_USER_HOME_ARGS=(--gradle-user-home "$HOME/.gradle")
    shift
fi

command -v gradle-profiler >/dev/null 2>&1 || {
    echo "gradle-profiler is not on PATH. Install it with: brew install gradle-profiler" >&2
    exit 1
}

VERSION="$(sed -n 's/^numverify\.buildLogic\.version=//p' gradle.properties | tail -1)"
[[ -n "$VERSION" ]] || {
    echo "numverify.buildLogic.version is missing from gradle.properties" >&2
    exit 1
}

# Always republish. The `_mvn` scenarios resolve a pinned version, so without this step an edit to
# build-logic would be measured on one side of the comparison and not the other.
echo "==> Publishing convention plugins $VERSION to mavenLocal"
./gradlew -p build-logic publishToMavenLocal --quiet

ARTIFACT_DIR="${HOME}/.m2/repository/com/phundal/numverify/buildlogic/convention/${VERSION}"
[[ -f "${ARTIFACT_DIR}/convention-${VERSION}.jar" ]] || {
    echo "Expected ${ARTIFACT_DIR}/convention-${VERSION}.jar after publishing, but it is not there" >&2
    exit 1
}

# Configure both modes once before spending minutes benchmarking them, so a broken settings script
# fails in seconds rather than halfway through the run.
echo "==> Checking both modes configure"
for source in includedBuild maven; do
    ./gradlew "-Pnumverify.buildLogic.source=${source}" help --quiet >/dev/null
    echo "    ${source}: ok"
done

OUTPUT_DIR="build/profiler/buildlogic-$(date +%Y%m%d-%H%M%S)"

echo "==> Benchmarking into ${OUTPUT_DIR}"
gradle-profiler \
    --benchmark \
    --project-dir . \
    --scenario-file performance.scenarios \
    --group buildlogic \
    --output-dir "$OUTPUT_DIR" \
    "${GRADLE_USER_HOME_ARGS[@]+"${GRADLE_USER_HOME_ARGS[@]}"}" \
    "$@"

echo
echo "Results (output lands under build/, which .gitignore already covers):"
echo "  ${OUTPUT_DIR}/benchmark.html"
echo "  ${OUTPUT_DIR}/benchmark.csv"
echo
echo "Compare within each pair — cfg_ib vs cfg_mvn, cfgcold_ib vs cfgcold_mvn, abi_ib vs abi_mvn."
echo "abi_* should come out at parity; if it does not, treat the other pairs as noise."
