/*
 * Copyright (c) 2017 MuleSoft, Inc. This software is protected under international
 * copyright law. All use of this software is subject to MuleSoft's Master Subscription
 * Agreement (or other master license agreement) separately entered into in writing between
 * you and MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.tools.migration.integration;

import org.junit.Ignore;
import org.junit.Test;

public class EmptyAppTestCase extends EndToEndTestCase {

  @Test
  @Ignore
  public void mavenized() throws Exception {
    simpleCase("emptyMvn");
  }

  @Test
  public void notMavenized() throws Exception {
    simpleCase("empty");
  }
}