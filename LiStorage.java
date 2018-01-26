

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LiStorage {

  private class FileController {
    private List<File> dirs;
    private List<String> imagesExt;
    private boolean isFirstAddFile=true;
    private List<String> musicExt;

    {
      this.imagesExt = Arrays.asList("jpeg", "jpg", "png", "bmp");
      this.musicExt = Arrays.asList("wav", "mp3", "aac", "ogg");
      this.dirs=new ArrayList<>();
    }


    private void configDirs(String path) {
      if (this.isFirstAddFile) {
        File dir = new File(path);
        if (!dir.isHidden() && dir.isDirectory()) {
          this.dirs.add(dir);
        }
        this.isFirstAddFile = false;
      }
      for (File file : getDirList(path)) {
        configDirs(file.getAbsolutePath());
      }
    }

    private List<File> getDirectories() {
      return this.dirs;
    }

    private List<File> getDirList(String path) {
      List<File> dirList = new ArrayList<>();
      File[] subDirs = new File(path).listFiles();
      if (subDirs != null) {
        for (File file : subDirs) {
          if (file.isDirectory() && !file.isHidden()) {
            this.dirs.add(file);
            dirList.add(file);
          }
        }
      }
      return dirList;
    }

    private boolean isValidImage(File file) {
      String fileName = file.getName();
      return this.imagesExt.contains(fileName.substring(fileName.lastIndexOf(46) + 1, fileName.length()).toLowerCase());
    }

    private boolean isValidMusic(File file) {
      String fileName = file.getName();
      return this.musicExt.contains(fileName.substring(fileName.lastIndexOf(46) + 1, fileName.length()).toLowerCase());
    }

    private List<File> getMusics(List<File> dirs) {
      List<File> fileList = new ArrayList<>();
      for (File dir : dirs) {
        File[] files = dir.listFiles();
        if (files != null) {
          for (File file : files) {
            if (!file.isDirectory() && isValidMusic(file)) {
              fileList.add(file);
            }
          }
        }
      }
      return fileList;
    }

    private List<File> getImages(List<File> dirs) {
      List<File> imagesList = new ArrayList<>();
      for (File dir : dirs) {
        File[] files = dir.listFiles();
        if (files != null) {
          for (File file : files) {
            if (!file.isDirectory() && isValidImage(file)) {
              imagesList.add(file);
            }
          }
        }
      }
      return imagesList;
    }
  }

  public interface OnImageReceived {
    void onReceive(List<File> imageList);

    void onError(String error);
  }

  public interface OnMusicReceived {
    void onReceive(List<File> musicList);

    void onError(String error);
  }

  public void getMusic(String dirPath, OnMusicReceived onMusicReceived) {
    FileController getFile = new FileController();
    getFile.configDirs(dirPath);
    List<File> musics = getFile.getMusics(getFile.dirs);
    if (musics.size() == 0) {
      onMusicReceived.onError("No Such Directory Or Empty Directory");
    } else {
      onMusicReceived.onReceive(musics);
    }
  }

  public void getImages(String dirPath, OnImageReceived onImageReceived) {
    FileController getFile = new FileController();
    getFile.configDirs(dirPath);
    List<File> images = getFile.getImages(getFile.dirs);
    if (images.size() == 0) {
      onImageReceived.onError("No Such Directory Or Empty Directory");
    } else {
      onImageReceived.onReceive(images);
    }
  }

  public List<File> getDirectories(String path) {
    FileController fileController = new FileController();
    fileController.configDirs(path);
    return fileController.getDirectories();
  }
}