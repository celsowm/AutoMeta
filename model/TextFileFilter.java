/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author celso
 */
public class TextFileFilter implements FileFilter {

    @Override
  public boolean accept(File pathname) {

    if (pathname.getName().endsWith(".txt"))
      return true;
    if (pathname.getName().endsWith(".text"))
      return true;
    return false;
  }
}
