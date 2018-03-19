/*
 * Copyright (c) 2017 MuleSoft, Inc. This software is protected under international
 * copyright law. All use of this software is subject to MuleSoft's Master Subscription
 * Agreement (or other master license agreement) separately entered into in writing between
 * you and MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.tools.migration.library.step.other;

import static com.mulesoft.tools.migration.project.structure.ProjectType.MULE_APPLICATION;

import java.util.HashSet;
import java.util.Set;
import com.mulesoft.tools.migration.engine.task.DefaultMigrationTask;
import com.mulesoft.tools.migration.engine.step.MigrationStep;
import com.mulesoft.tools.migration.engine.task.Version;
import com.mulesoft.tools.migration.engine.task.Version.VersionBuilder;
import com.mulesoft.tools.migration.project.structure.ProjectType;

/**
 * JUST A CONCEPT todo delete
 * 
 * @author Mulesoft Inc.
 */
public class MunitMigrationTask extends DefaultMigrationTask {

  @Override
  public Version getFrom() {
    return new VersionBuilder().withMajor("3").withMinor("8").withRevision("0").build();
  }

  @Override
  public Version getTo() {
    return new VersionBuilder().withMajor("4").withMinor("0").withRevision("0").build();
  }


  @Override
  public ProjectType getProjectType() {
    return MULE_APPLICATION;
  }


  @Override
  public Set<MigrationStep> getSteps() {
    Set<MigrationStep> steps = new HashSet<>();

    return steps;
  }

  @Override
  public String getDescription() {
    return null;
  }
}
