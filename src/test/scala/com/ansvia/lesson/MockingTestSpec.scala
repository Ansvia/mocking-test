package com.ansvia.lesson

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito

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
        "bisa generate voucher" >> {
            val robin = User("robin", "r@ansvia.com")

            val robinsVoucher = BasicEngine.voucher.generate(robin)

            robinsVoucher must_!= ""

            BasicEngine.voucher.isValid(robin, robinsVoucher) must beTrue
        }
    }
}
