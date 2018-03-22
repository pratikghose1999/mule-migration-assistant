/*
 * Copyright (c) 2017 MuleSoft, Inc. This software is protected under international
 * copyright law. All use of this software is subject to MuleSoft's Master Subscription
 * Agreement (or other master license agreement) separately entered into in writing between
 * you and MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.tools.migration.pom.model;

import com.mulesoft.tools.migration.pom.model.predicate.DependencyPredicate;
import com.mulesoft.tools.migration.pom.model.utils.PomModelDependencyUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkArgument;
import static com.mulesoft.tools.migration.pom.model.utils.PomModelDependencyUtils.toDependency;
import static java.util.stream.Collectors.toList;

/**
 * The pom model.
 *
 * @author Mulesoft Inc.
 * @since 1.0.0
 */
public class PomModel {

  private final Model model;

  private PomModel(Model model) {
    this.model = model;
  }

  public PomModel() {
    this.model = new Model();
  }

  /**
   * Retrieves the list of dependencies in the pom model.
   * 
   * @return the list of dependencies declared in the pom model
   */
  public List<PomModelDependency> getDependencies() {
    return model.getDependencies().stream().map(PomModelDependencyUtils::toPomModelDependency).collect(toList());
  }

  /**
   * Sets the dependencies in the pom model.
   * 
   * @param dependencies the list of the dependencies to be set in the pom model
   */
  public void setDependencies(List<PomModelDependency> dependencies) {
    model.setDependencies(dependencies.stream().map(PomModelDependencyUtils::toDependency).collect(toList()));
  }

  /**
   * Adds a dependency to the pom model dependencies list. The dependency is not added if another dependency with same artifact
   * id, group id and version is already present in the dependencies list
   * 
   * @param dependency the dependencies to be added to the pom model
   * @return true if the dependency was added to the dependency list; false otherwise
   */
  public boolean addDependency(PomModelDependency dependency) {
    DependencyPredicate predicate = new DependencyPredicate(dependency);
    if (dependency == null || model.getDependencies().stream().anyMatch(predicate.isSameDependency())) {
      return false;
    }
    model.addDependency(toDependency(dependency));
    return true;
  }

  /**
   * Removes a dependency from the pom model dependencies list.
   *
   * @param dependency the dependency to be removed from the dependencies list
   * @return true if the dependency was removed from the dependency list; false otherwise
   */
  public boolean removeDependency(PomModelDependency dependency) {
    int originalNumDependencies = model.getDependencies().size();
    DependencyPredicate predicate = new DependencyPredicate(dependency);
    List dependencies = model.getDependencies().stream().filter(predicate.isSameDependency().negate()).collect(toList());
    model.setDependencies(dependencies);
    return model.getDependencies().size() == originalNumDependencies - 1;
  }

  /**
   * Retrieves the packaging type declared in the pom.
   * 
   * @return the packaging type
   */
  public String getPackaging() {
    return model.getPackaging();
  }

  /**
   * Retrieves a deep copy of the maven model.
   *
   * @return the model deep's copy
   */
  public Model getMavenModelCopy() {
    return model.clone();
  }

  /**
   * Sets the packaging type in the pom.
   * 
   * @param packaging the packaging type to be set
   */
  public void setPackaging(String packaging) {
    model.setPackaging(packaging);
  }

  /**
   * Retrieves the artifact id declared in the pom.
   * 
   * @return the artifact id declared in the pom
   */
  public String getArtifactId() {
    return model.getArtifactId();
  }

  /**
   * Sets the artifact id in the pom.
   *
   * @param artifactId the artifact id to be set
   */
  public void setArtifactId(String artifactId) {
    model.setArtifactId(artifactId);
  }

  /**
   * Retrieves the group id declared in the pom.
   *
   * @return the group id declared in the pom
   */
  public String getGroupId() {
    return model.getGroupId();
  }

  /**
   * Sets the group id in the pom.
   *
   * @param groupId the group id to be set
   */
  public void setGroupId(String groupId) {
    model.setGroupId(groupId);
  }

  /**
   * Retrieves the version declared in the pom.
   *
   * @return the version declared in the pom
   */
  public String getVersion() {
    return model.getVersion();
  }

  /**
   * Sets the version in the pom.
   *
   * @param version the version to be set
   */
  public void setVersion(String version) {
    model.setVersion(version);
  }

  /**
   * Retrieves the name declared in the pom.
   *
   * @return the name declared in the pom
   */
  public String getName() {
    return model.getName();
  }

  /**
   * Sets the name in the pom.
   *
   * @param name the name to be set
   */
  public void setName(String name) {
    model.setName(name);
  }

  /**
   * Retrieves the properties declared in the pom.
   * 
   * @return the properties declared in the pom
   */
  public Properties getProperties() {
    return model.getProperties();
  }

  /**
   * Sets the properties in the pom.
   * 
   * @param properties the properties to be set
   */
  public void setProperties(Properties properties) {
    model.setProperties(properties);
  }

  /**
   * Sets a key-valued pair in the properties map.
   * 
   * @param key the property key
   * @param value the property value
   */
  public void addProperty(String key, String value) {
    model.addProperty(key, value);
  }

  /**
   * The pom model builder. It builds the pom model based on the pom location in the filesystem.
   *
   * @author Mulesoft Inc.
   * @since 1.0.0
   */
  public static class PomModelBuilder {

    private final MavenXpp3Reader mavenReader = new MavenXpp3Reader();
    private Path pomPath;

    public PomModelBuilder withPom(Path pomPath) {
      this.pomPath = pomPath;
      return this;
    }

    public PomModel build() throws IOException, XmlPullParserException {
      checkArgument(pomPath != null, "Pom path should not be null");
      if (!pomPath.toFile().exists()) {
        return new PomModel();
      }
      Model model = getModel(pomPath);
      return new PomModel(model);
    }

    private Model getModel(Path pomPath) throws IOException, XmlPullParserException {
      Model model;
      try (BufferedReader reader = new BufferedReader(new FileReader(pomPath.toFile()))) {
        model = mavenReader.read(reader);
      }

      return model;
    }
  }
}
