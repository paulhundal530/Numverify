package com.phundal.numverify.common.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class BackStackTest {

    private val home = Destination("home")
    private val details = Destination("details")

    @Test
    fun `starts on the start destination`() {
        val stack = BackStack.of(home)

        assertEquals(home, stack.current)
        assertFalse(stack.canNavigateBack)
    }

    @Test
    fun `push moves to the new destination and remembers the old one`() {
        val stack = BackStack.of(home).push(details)

        assertEquals(details, stack.current)
        assertTrue(stack.canNavigateBack)
        assertEquals(listOf(home, details), stack.entries)
    }

    @Test
    fun `pushing the current destination again is a no-op`() {
        val stack = BackStack.of(home).push(details)

        assertSame(stack, stack.push(details))
    }

    @Test
    fun `pop returns to the previous destination`() {
        val stack = BackStack.of(home).push(details).pop()

        assertEquals(home, stack.current)
        assertFalse(stack.canNavigateBack)
    }

    @Test
    fun `pop at the start destination leaves the stack unchanged`() {
        val stack = BackStack.of(home)

        assertSame(stack, stack.pop())
    }

    @Test
    fun `a blank route is rejected`() {
        assertThrows(IllegalArgumentException::class.java) { Destination("  ") }
    }
}
