/*
 * Copyright (C) 2020 PatrickKR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
import sun.security.jca.Providers
import java.net.URL
import java.security.SecureRandom
import javax.net.ssl.HttpsURLConnection

fun main(args: Array<String>) {
    val array = ArrayList<String>()
    while (true) {
        val input = readLine()
        if (input.isNullOrBlank()) {
            if (array.count() > 0) { //에러 방지
                //주작 아닙니다 현재 시각 21:33 (수동 입력 ㅈㅅ)
                val picked = array.shuffled(SecureRandom.getInstance("SHA1PRNG", Providers.getSunProvider())).subList(0, 1).mapIndexed { index, name ->
                    "${index + 1}. $name" //열심히 셔플 셔플
                }.joinToString(System.lineSeparator()) //랜덤 방식: Java SecureRandom 중에서 SHA1PRNG algorithm (sha1 기반 랜덤 시스템)으로 입력 을 열심히 섞습니다. 그리고 나서, sublist로 하나 가져와봅니다
                println(picked) //공정성 위해 로그에도 박기
                val data = """{"embed":{"author":{"name":"PatrickKR"},"title":"추첨 결과 발표!","type":"rich","description":"수령 불가 인원이 1명 (해외 거주) 발생하여 재추첨 진행하기로 하였습니다.
                    |1명 재추첨 하겠습니다.
                    |총 신청 인원: ${array.count()} (${array.joinToString(", ")})
                    |
                    |$picked","footer":{"text":"from PatrickKR"}}}
                    """.trimMargin().replace("\n", "\\n")
                println(data) //json check
                (URL("https://discord.com/api/channels/612214966321152005/messages").openConnection() as HttpsURLConnection).run { //discord channel message
                    setRequestProperty("Content-Type", "application/json") //json type
                    addRequestProperty("Authorization", "Bot ${args[0]}") //auth: my bot token
                    addRequestProperty("User-Agent", "Event Discord/1.0-SNAPSHOT") //user agent는 랜덤이 차라리 덜 걸러진다는 ㅋㅋㅋ

                    doOutput = true //output에 data 들어간다는거 고지
                    requestMethod = "POST" //post 방식 (넣는거니까)
                    outputStream.write(data.toByteArray()) //out stream에 data bytearray로

                    inputStream //input 안가져오면 안보내짐 ㅋㅋ
                }
                return
            }
        } else {
            array.add(input) //queue에 추가
        }
    }
}
