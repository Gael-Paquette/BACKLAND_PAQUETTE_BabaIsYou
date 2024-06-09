package fr.esiee.babaisyou.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The {@code ImagesLoader} class is responsible for loading and storing images for objects and text used in the game.
 */
public class ImagesLoader {
  private final HashMap<String, Image> imagesObject = new HashMap<>();
  private final HashMap<String, Image> imagesText = new HashMap<>();

  /**
   * Creates the path for a text image file given its name and type.
   *
   * @param name the name of the image
   * @param type the type of the image (NOUNS, OPERATORS, PROPERTIES)
   * @return the constructed path for the image file
   * @throws NullPointerException if {@code name} or {@code type} is null
   */
  private String createPathText(String name, Type type) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(type);
    return switch (type) {
      case NOUNS -> "/images/NOUNS/" + name + "/" + name + ".gif";
      case OPERATORS -> "/images/OPERATORS/" + name + "/" + name + ".gif";
      case PROPERTIES -> "/images/PROPERTIES/" + name + "/" + name + ".gif";
    };
  }

  /**
   * Creates the path for an object image file given its name.
   *
   * @param name the name of the image
   * @return the constructed path for the image file
   * @throws NullPointerException if {@code name} is null
   */
  private String createPathObject(String name) {
    Objects.requireNonNull(name);
    return "/images/NOUNS/" + name + "/" + name + "_GIF.gif";
  }

  /**
   * Loads an image from the specified path and associates it with the given name.
   *
   * @param name the name to associate with the image
   * @param path the path to the image file
   * @param isText true if the image is a text image, false if it is an object image
   * @throws NullPointerException if {@code name} or {@code path} is null
   * @throws IllegalStateException if the image file is not found
   */
  private void loadImage(String name, String path, boolean isText) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(path);
    if (isText) {
      var imageUrl = ImagesLoader.class.getResource(path);
      if (imageUrl == null) {
        throw new IllegalStateException("Image not found: " + path);
      }
      imagesText.put(name, new ImageIcon(imageUrl).getImage());
    } else {
      var imageUrl = ImagesLoader.class.getResource(path);
      if (imageUrl == null) {
        throw new IllegalStateException("Image not found: " + path);
      }
      imagesObject.put(name, new ImageIcon(imageUrl).getImage());
    }
  }

  /**
   * Initializes the images for nouns by loading them from the given list of names.
   *
   * @param nouns the list of noun names to load
   * @throws NullPointerException if {@code nouns} is null
   */
  private void initImagesNouns(List<String> nouns) {
    Objects.requireNonNull(nouns);
    nouns.forEach(noun -> {
      var objectPath = createPathObject(noun);
      var textPath = createPathText(noun, Type.NOUNS);
      loadImage(noun, objectPath, false);
      loadImage(noun, textPath, true);
    });
  }

  /**
   * Initializes the images for operators by loading them from the given list of names.
   *
   * @param operators the list of operator names to load
   * @throws NullPointerException if {@code operators} is null
   */
  private void initImagesOperators(List<String> operators) {
    Objects.requireNonNull(operators);
    operators.forEach(operator -> {
      var textPath = createPathText(operator, Type.OPERATORS);
      loadImage(operator, textPath, true);
    });
  }

  /**
   * Initializes the images for properties by loading them from the given list of names.
   *
   * @param properties the list of property names to load
   * @throws NullPointerException if {@code properties} is null
   */
  private void initImagesProperties(List<String> properties) {
    Objects.requireNonNull(properties);
    properties.forEach(property -> {
      var textPath = createPathText(property, Type.PROPERTIES);
      loadImage(property, textPath, true);
    });
  }

  /**
   * Constructs an {@code ImagesLoader} instance and initializes the images for nouns, operators, and properties.
   *
   * @param nouns the list of noun names to load
   * @param operators the list of operator names to load
   * @param properties the list of property names to load
   * @throws NullPointerException if any of the lists are null
   */
  public ImagesLoader(List<String> nouns, List<String> operators, List<String> properties) {
    initImagesNouns(nouns);
    initImagesOperators(operators);
    initImagesProperties(properties);
  }

  /**
   * Retrieves the object image associated with the given name.
   *
   * @param name the name of the image to retrieve
   * @return the object image associated with the given name, or {@code null} if no image is found
   */
  public Image getImageObject(String name) {
    return imagesObject.getOrDefault(name, null);
  }

  /**
   * Retrieves the text image associated with the given name.
   *
   * @param name the name of the image to retrieve
   * @return the text image associated with the given name, or {@code null} if no image is found
   */
  public Image getImageText(String name) {
    return imagesText.getOrDefault(name, null);
  }

  /**
   * Retrieves all loaded object images.
   *
   * @return a map of all loaded object images, where the keys are image names and the values are the images
   */
  public Map<String, Image> getImagesObject() {
    return imagesObject;
  }

  /**
   * Retrieves all loaded text images.
   *
   * @return a map of all loaded text images, where the keys are image names and the values are the images
   */
  public Map<String, Image> getImagesText() {
    return imagesText;
  }
}
