package fr.esiee.babaisyou.view;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ImagesLoader {
  private final HashMap<String, Image> imagesObject = new HashMap<>();
  private final HashMap<String, Image> imagesText = new HashMap<>();

  private Path createPathText(String name, Type type) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(type);
    return switch (type) {
      case NOUNS -> Paths.get("images/NOUNS/" + name + "/" + name + ".png");
      case OPERATORS -> Paths.get("images/OPERATORS/" + name + "/" + name + ".png");
      case PROPERTIES -> Paths.get("images/PROPERTIES/" + name + "/" + name + ".png");
    };
  }

  private Path createPathObject(String name) {
    Objects.requireNonNull(name);
    return Paths.get("images/NOUNS/" + name + "/" + name + "_GIF.png");
  }

  private void loadImage(String name, Path path, boolean isText) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(path);
    if (isText) {
      try {
        imagesText.put(name, new ImageIcon(path.toUri().toURL()).getImage());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      try {
        imagesObject.put(name, new ImageIcon(path.toUri().toURL()).getImage());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void initImagesNouns(List<String> nouns) {
    Objects.requireNonNull(nouns);
    nouns.forEach(noun -> {
      var objectPath = createPathObject(noun);
      var textPath = createPathText(noun, Type.NOUNS);
      loadImage(noun, objectPath, false);
      loadImage(noun, textPath, true);
    });
  }

  private void initImagesOperators(List<String> operators) {
    Objects.requireNonNull(operators);
    operators.forEach(operator -> {
      var textPath = createPathText(operator, Type.OPERATORS);
      loadImage(operator, textPath, true);
    });
  }

  private void initImagesProperties(List<String> properties) {
    Objects.requireNonNull(properties);
    properties.forEach(property -> {
      var textPath = createPathText(property, Type.PROPERTIES);
      loadImage(property, textPath, true);
    });
  }

  public ImagesLoader(List<String> nouns, List<String> operators, List<String> properties) {
    initImagesNouns(nouns);
    initImagesOperators(operators);
    initImagesProperties(properties);
  }

  public Image getImageObject(String name) {
    return imagesObject.getOrDefault(name, null);
  }

  public Image getImageText(String name) {
    return imagesText.getOrDefault(name, null);
  }

  public HashMap<String, Image> getImagesObject() {
    return imagesObject;
  }

  public HashMap<String, Image> getImagesText() {
    return imagesText;
  }
}
