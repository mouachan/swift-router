/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class RouterTest {
    
    @Test
    public void testEvaluateRouter() {
        given()
               .body("{\n" +
                     "    \"event\": {\n" +
                     "        \"receiverAddress\": \"BNPAFRPP\",\n" +
                     "        \"messageType\": {\n" +
                     "          \"code\": \"MT012\"" +
                     "          },\n" +
                     "        \"TRN\": \"Test\",\n" +
                     "        \"document\": {\n" +
                     "          \"data\": \"r{4:5103:EBA7{5:6\"" +
                     "          }\n" +
                     "    }\n" +
                     "}")
               .contentType(ContentType.JSON)
          .when()
               .post("/router")
          .then()
             .statusCode(200)
               .body("codeRoutage", is("CHG02"));
    }
}