package com.ansvia.lesson

import java.util.Date

/**
 * Author: robin (r@ansvia.com)
 */



case class User(name:String, email:String)

trait TimeProvider {
    def currentTimeMillis = System.currentTimeMillis()
    def getCurrentDate = new Date()
}

trait VoucherEngine {

    val timeProvider:TimeProvider

    def generate(user:User):String

    def isValid(user:User, code:String):Boolean

    def onErrorCallback(code:String, errorInfo:String){}
}

trait EngineModule {
    val timeProvider:TimeProvider
    val voucher:VoucherEngine
}


/**
 * Implementasi Engine menggunakan settingan dasar.
 */
trait BasicEngine extends EngineModule {

    // gunakan time provider bawaan asli
    override val timeProvider: TimeProvider = new TimeProvider {}

    /**
     * Implementasi voucher engine.
     * @param _timeProvider time provider yang akan digunakan sebagai basis waktu.
     */
    class MyVoucherEngine(_timeProvider: TimeProvider) extends VoucherEngine {
        override val timeProvider: TimeProvider = _timeProvider


        override def generate(user: User): String = {
            user.name.hashCode.toString + user.email.hashCode.toString + ":" + timeProvider.currentTimeMillis.toString
        }

        /**
         * Periksa apakah suatu voucher valid atau tidak.
         * Rules:
         *      1. Hash code-nya harus sama dengan nama user.
         *      2. Voucher-nya belum kadaluarsa (lebih dari satu hari setelah diterbitkan).
         * @param user user yang akan mendapatkan voucher.
         * @param code kode voucher yang akan diperiksa.
         * @return
         */
        override def isValid(user:User, code: String): Boolean = {
            code.split(':').toList match {
                case hashStr :: timestampStr :: Nil =>
                    val time = timestampStr.toLong

                    hashStr == (user.name.hashCode.toString + user.email.hashCode.toString) &&
                        (time + 864E+7) > timeProvider.currentTimeMillis

                case _ =>
                    false
            }
        }

        override def onErrorCallback(code: String, errorInfo: String){
            super.onErrorCallback(code, errorInfo)

        }
    }

    override lazy val voucher: VoucherEngine = new MyVoucherEngine(timeProvider)
}

object Engine extends BasicEngine


