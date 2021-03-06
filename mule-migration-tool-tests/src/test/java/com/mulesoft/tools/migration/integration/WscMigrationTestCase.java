/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.integration;

import org.mule.tck.junit4.rule.DynamicPort;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class WscMigrationTestCase extends EndToEndTestCase {

  @Rule
  public final DynamicPort httpPort = new DynamicPort("httpPort");

  @Parameters(name = "{0}")
  public static Object[] params() {
    return new Object[] {
        "wsc1",
        "wsc1Mvn"
    };
  }

  private final String appToMigrate;

  public WscMigrationTestCase(String appToMigrate) {
    this.appToMigrate = appToMigrate;
  }

  @Test
  public void test() throws Exception {
    simpleCase(appToMigrate, "-M-DhttpPort=" + httpPort.getValue());
  }
}
