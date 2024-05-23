package fr.esiee.babaisyou.view;

import fr.esiee.babaisyou.model.GameBoard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ImagesLoader {
  private final HashMap<String, BufferedImage> imagesObject = new HashMap<>();
  private final HashMap<String, BufferedImage> imagesText = new HashMap<>();

  private String createPathText(String name, Type type) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(type);
    return switch (type) {
      case NOUNS -> "/images/NOUNS/" + name + "/" + name + ".png";
      case OPERATORS -> "/images/OPERATORS/" + name + "/" + name + ".png";
      case PROPERTIES -> "/images/PROPERTIES/" + name + "/" + name + ".png";
    };
  }

  private String createPathObject(String name) {
    Objects.requireNonNull(name);
    return "/images/NOUNS/" + name + "/" + name + "_GIF.png";
  }

  private void loadImage(String name, String path, boolean isText) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(path);
    try (InputStream stream = ImagesLoader.class.getResourceAsStream(path)) {
      System.out.println(path);
      System.out.println(stream);
      assert stream != null;
      if (isText) {
        imagesText.put(name, ImageIO.read(stream));
      } else {
        imagesObject.put(name, ImageIO.read(stream));
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void initImagesNouns(List<String> nouns) {
    Objects.requireNonNull(nouns);
    nouns.forEach(noun -> {
      var objectPath = createPathObject(noun);
      var textPath = createPathText(noun, Type.NOUNS);
      System.out.println(objectPath);
      System.out.println(textPath);
      loadImage(noun, objectPath, false);
      System.out.println("test");
      loadImage(noun, textPath, true);
      System.out.println("test2");
    });
  }

  private void initImagesOperators(List<String> operators) {
    Objects.requireNonNull(operators);
    operators.forEach(operator -> {
      var textPath = createPathText(operator, Type.OPERATORS);
      System.out.println(textPath);
      loadImage(operator, textPath, true);
    });
  }

  private void initImagesProperties(List<String> properties) {
    Objects.requireNonNull(properties);
    properties.forEach(propertie -> {
      var textPath = createPathText(propertie, Type.PROPERTIES);
      System.out.println(textPath);
      loadImage(propertie, textPath, true);
    });
  }

  public ImagesLoader(List<String> nouns, List<String> operators, List<String> properties) {
    initImagesNouns(nouns);
    initImagesOperators(operators);
    initImagesProperties(properties);
  }

  public BufferedImage getImageObject(String name) {
    return imagesObject.getOrDefault(name, null);
  }

  public BufferedImage getImageText(String name) {
    return imagesText.getOrDefault(name, null);
  }

  public HashMap<String, BufferedImage> getImagesObject() {
    return imagesObject;
  }

  public HashMap<String, BufferedImage> getImagesText() {
    return imagesText;
  }
}
