/*
 * Copyright 2021 Typelevel
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

package bobcats

import cats.effect.kernel.{Async, Resource, Sync}

private[bobcats] trait CryptoCompanionPlatform {

  def forSync[F[_]](implicit F: Sync[F]): Resource[F, Crypto[F]] =
    Resource.eval(F.delay {
      val providers = Providers.get()
      new UnsealedCrypto[F] {
        override def hash: Hash[F] = Hash.forProviders(providers)
        override def hmac: Hmac[F] = Hmac.forProviders(providers)
        override def cipher: Cipher[F] = Cipher.forSync[F]
      }
    })

  def forAsync[F[_]: Async]: Resource[F, Crypto[F]] = forSync
}
