package com.alphastudio.CrossBorderCore.config;

public class BorderConfig {
  public enum BorderSide { NORTH, SOUTH, EAST, WEST }

  private final String world;
  private final BorderSide side;
  private final double coordinate;
  private final String targetServer;
  private final String targetWorld;
  private final BorderSide targetSide;
  private final double targetCoordinate;
  private final double targetOffset;
  private final boolean showParticles;

  public BorderConfig(
      String world,
      BorderSide side,
      double coordinate,
      String targetServer,
      String targetWorld,
      BorderSide targetSide,
      double targetCoordinate,
      double targetOffset,
      boolean showParticles) {
    this.world = world;
    this.side = side;
    this.coordinate = coordinate;
    this.targetServer = targetServer;
    this.targetWorld = targetWorld;
    this.targetSide = targetSide;
    this.targetCoordinate = targetCoordinate;
    this.targetOffset = targetOffset;
    this.showParticles = showParticles;
  }

  public String getWorld() {
    return world;
  }

  public BorderSide getSide() {
    return side;
  }

  public double getCoordinate() {
    return coordinate;
  }

  public String getTargetServer() {
    return targetServer;
  }

  public boolean isLocalWorldTransfer() {
    return targetServer == null || targetServer.isBlank();
  }

  public boolean isRemoteServerTransfer() {
    return !isLocalWorldTransfer();
  }

  public String getTargetWorld() {
    return targetWorld;
  }

  public BorderSide getTargetSide() {
    return targetSide;
  }

  public double getTargetCoordinate() {
    return targetCoordinate;
  }

  public double getTargetOffset() {
    return targetOffset;
  }

  public boolean isShowParticles() {
    return showParticles;
  }

  @Override
  public String toString() {
    return "BorderConfig{" +
        "world='" + world + '\'' +
        ", side=" + side +
        ", coordinate=" + coordinate +
        ", targetServer='" + targetServer + '\'' +
        ", targetWorld='" + targetWorld + '\'' +
        ", targetSide=" + targetSide +
        ", targetCoordinate=" + targetCoordinate +
        ", targetOffset=" + targetOffset +
        ", showParticles=" + showParticles +
        '}';
  }
}
