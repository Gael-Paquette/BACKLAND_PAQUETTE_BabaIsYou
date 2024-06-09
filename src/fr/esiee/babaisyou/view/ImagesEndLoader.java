package fr.esiee.babaisyou.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The {@code ImagesEndLoader} class is responsible for loading and storing images used at the end of the game.
 */
public class ImagesEndLoader {
  private final HashMap<String, Image> images = new HashMap<>();

  /**
   * Constructs the path for an image file given its name.
   *
   * @param name the name of the image
   * @return the constructed path for the image file
   * @throws NullPointerException if {@code name} is null
   */
  private String createPath(String name) {
    Objects.requireNonNull(name);
    return "/images/END/" + name + ".png";
  }

  /**
   * Loads an image from the specified path and associates it with the given name.
   *
   * @param name the name to associate with the image
   * @param path the path to the image file
   * @throws NullPointerException if {@code name} or {@code path} is null
   * @throws IllegalStateException if the image file is not found
   */
  private void loadImage(String name, String path) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(path);

    var imageUrl = ImagesEndLoader.class.getResource(path);
    if (imageUrl == null) {
      throw new IllegalStateException("Image not found: " + path);
    }
    images.put(name, new ImageIcon(imageUrl).getImage());
  }

  /**
   * Initializes the images by loading them from the given list of image names.
   *
   * @param imagesEnd the list of image names to load
   * @throws NullPointerException if {@code imagesEnd} is null
   */
  private void initImages(List<String> imagesEnd) {
    Objects.requireNonNull(imagesEnd);
    imagesEnd.forEach(image -> {
      var path = createPath(image);
      loadImage(image, path);
    });
  }

  /**
   * Constructs an {@code ImagesEndLoader} instance and initializes the images.
   *
   * @param imagesEnd the list of image names to load
   * @throws NullPointerException if {@code imagesEnd} is null
   */
  public ImagesEndLoader(List<String> imagesEnd) {
    initImages(imagesEnd);
  }

  /**
   * Retrieves the image associated with the given name.
   *
   * @param name the name of the image to retrieve
   * @return the image associated with the given name, or {@code null} if no image is found
   */
  public Image getImage(String name) { return images.getOrDefault(name, null); }

  /**
   * Retrieves all loaded images.
   *
   * @return a map of all loaded images, where the keys are image names and the values are the images
   */
  public Map<String, Image> getImages() { return images; }
}
