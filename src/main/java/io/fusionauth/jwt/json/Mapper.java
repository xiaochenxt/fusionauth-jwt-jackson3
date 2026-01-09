/*
 * Copyright (c) 2016-2019, FusionAuth, All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package io.fusionauth.jwt.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import io.fusionauth.jwt.InvalidJWTException;
import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;

/**
 * Serialize and de-serialize JWT header and payload.
 *
 * @author Daniel DeGroff
 */
public class Mapper {
  private final static JsonMapper JSON_MAPPER;

  public static <T> T deserialize(byte[] bytes, Class<T> type) throws InvalidJWTException {
    try {
      return JSON_MAPPER.readValue(bytes, type);
    } catch (JacksonException e) {
      throw new InvalidJWTException("The JWT could not be de-serialized.", e);
    }
  }

  public static <T> T deserialize(InputStream is, Class<T> type) throws InvalidJWTException {
    try {
      return JSON_MAPPER.readValue(is, type);
    } catch (JacksonException e) {
      throw new InvalidJWTException("The input stream could not be de-serialized.", e);
    }
  }

  public static byte[] prettyPrint(Object object) throws InvalidJWTException {
    try {
      return JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(object);
    } catch (JacksonException e) {
      throw new InvalidJWTException("The object could not be serialized.", e);
    }
  }

  public static byte[] serialize(Object object) throws InvalidJWTException {
    try {
      return JSON_MAPPER.writeValueAsBytes(object);
    } catch (JacksonException e) {
      throw new InvalidJWTException("The JWT could not be serialized.", e);
    }
  }

  static {
    JSON_MAPPER = JsonMapper.builder().changeDefaultPropertyInclusion(h -> h.withValueInclusion(JsonInclude.Include.NON_NULL)
            .withContentInclusion(JsonInclude.Include.NON_NULL))
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
        .enable(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS).build();
  }
}
