package com.example.moviebox

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class TabNavigationTest {

    @Test
    fun tabEnum_containsAllExpectedTabs() {
        val tabs = Tab.entries
        assertEquals("Should have exactly 4 navigation tabs", 4, tabs.size)
        assertEquals(Tab.HOME, tabs[0])
        assertEquals(Tab.EXPLORE, tabs[1])
        assertEquals(Tab.FAVORITES, tabs[2])
        assertEquals(Tab.PROFILE, tabs[3])
    }

    @Test
    fun tabEnum_hasCorrectLabelsAndIcons() {
        assertEquals("Home", Tab.HOME.label)
        assertEquals("Explore", Tab.EXPLORE.label)
        assertEquals("Favorites", Tab.FAVORITES.label)
        assertEquals("Profile", Tab.PROFILE.label)

        Tab.entries.forEach { tab ->
            assertNotNull("Selected icon should not be null", tab.selectedIcon)
            assertNotNull("Unselected icon should not be null", tab.unselectedIcon)
        }
    }
}
