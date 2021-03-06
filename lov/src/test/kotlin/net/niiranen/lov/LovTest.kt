/*
 * Copyright 2016 Mattias Niiranen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.niiranen.lov

import android.Manifest
import android.test.mock.MockContext
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * @suppress
 */
class LovTest {
    @Test fun addRationale() {
        Lov.addRationale("test", PermissionRationale(0, 0, 0, 0))
        assertEquals(PermissionRationale(0, 0, 0, 0), Lov.rationales["test"])
    }

    @Test fun removeRationale() {
        Lov.addRationale("test", PermissionRationale(0, 0, 0, 0))
        Lov.removeRationale("test")
        assertNull(Lov.rationales["test"])
    }

    @Test fun removeNonExistingRationale() {
        Lov.removeRationale("test")
        assertNull(Lov.rationales["test"])
    }

    @Test fun overwriteRationale() {
        Lov.addRationale("test", PermissionRationale(0, 0, 0, 0))
        Lov.addRationale("test", PermissionRationale(1, 2, 3, 4))
        assertEquals(PermissionRationale(1, 2, 3, 4), Lov.rationales["test"])
    }

    @Test fun requestPermission() {
        Lov.request(MockContext(), Manifest.permission.CAMERA).subscribe { permission ->
            assertNotNull(permission)
        }
    }

    @Test fun contextRequestPermissions() {
        MockContext().requestPermissions(Manifest.permission.CAMERA).subscribe {
            assertNotNull(it)
        }
    }

    @Test fun onPermissions() {
        val latch = CountDownLatch(1)
        Lov.onPermissions(MockContext(), Manifest.permission.CAMERA) {
            latch.countDown()
        }
        latch.await(10, TimeUnit.SECONDS)
    }

    @Test fun contextOnPermission() {
        val latch = CountDownLatch(1)
        MockContext().onPermissions(Manifest.permission.CAMERA) {
            latch.countDown()
        }
        latch.await(10, TimeUnit.SECONDS)
    }
}
