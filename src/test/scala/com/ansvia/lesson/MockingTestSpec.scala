package com.ansvia.lesson

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.specs2.specification.Scope

/**
 * Author: robin (r@ansvia.com)
 */



class MockingTestSpec extends Specification with Mockito {

    "Contoh mocking" >> {
        "bisa nge-mock method standar collection (list)" >> {

            val mCol = mock[java.util.List[String]]

            // setup mock
            mCol.get(0) returns "satu"
            mCol.get(1) returns "dua"

            // test
            mCol.get(0) must_== "satu"
            mCol.get(1) must_== "dua"

        }

        "bisa generate voucher" in new MockedEngine {}

        "masih valid kalau masih dalam rentang satu hari sejak diterbitkannya" in new MockedEngine {
            mEngine.voucher.isValid(user, voucher) must beTrue
        }

        "voucher jadi tidak valid kalau sudah lebih dari satu hari sejak diterbitkannya" in new MockedEngine {

            mEngine.timeProvider.currentTimeMillis returns (System.currentTimeMillis() + 864E+7.toLong)
            mEngine.voucher.isValid(user, voucher) must beFalse
        }
    }


    trait MockedEngine extends Scope {
        val user = User("robin", "r@ansvia.com")
        val voucher = Engine.voucher.generate(user)

        // setup our engine (for test purpose)
        val mEngine = new BasicEngine {

            // mock the time
            override val timeProvider: TimeProvider = mock[TimeProvider]
        }
    }

}
