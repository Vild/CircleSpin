package circlespin.graphic;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import java.io.*;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Texture implements Serializable {
  public static final int LINEAR = GL_LINEAR;
  public static final int NEAREST = GL_NEAREST;
  public static final int CLAMP = GL_CLAMP;
  public static final int CLAMP_TO_EDGE = GL12.GL_CLAMP_TO_EDGE;
  public static final int REPEAT = GL_REPEAT;
  /**
   *
   */
  private static final long serialVersionUID = 4482581106751332656L;
  public final int target = GL_TEXTURE_2D;
  public final int id;
  public final int width;
  public final int height;

  public Texture(String path) {
    this(path, NEAREST, CLAMP_TO_EDGE);
  }

  public Texture(String path, int filter) {
    this(path, filter, CLAMP_TO_EDGE);
  }

  public Texture(String path, int filter, int wrap) {
    InputStream input = null;
    try {
      input = new FileInputStream(path);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    PNGDecoder dec = null;
    ByteBuffer buf = null;
    try {
      dec = new PNGDecoder(input);
    } catch (IOException e) {
      e.printStackTrace();
    }

    width = dec.getWidth();
    height = dec.getHeight();

    // we will decode to RGBA format, i.e. 4 components or "bytes per pixel"
    final int bpp = 4;

    // create a new byte buffer which will hold our pixel data
    buf = BufferUtils.createByteBuffer(bpp * width * height);

    // decode the image into the byte buffer, in RGBA format
    try {
      dec.decode(buf, width * bpp, PNGDecoder.Format.RGBA);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // flip the buffer into "read mode" for OpenGL
    buf.flip();

    // Generally a good idea to enable texturing first
    glEnable(GL_TEXTURE_2D);

    // generate a texture handle or unique ID for this texture
    id = glGenTextures();

    // bind the texture
    glBindTexture(GL_TEXTURE_2D, id);

    // use an alignment of 1 to be safe
    // this tells OpenGL how to unpack the RGBA bytes we will specify
    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

    // set up our texture parameters
    // Setup filtering, i.e. how OpenGL will interpolate the pixels when
    // scaling up or down
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

    // Setup wrap mode, i.e. how OpenGL will handle pixels outside of the
    // expected range
    // Note: GL_CLAMP_TO_EDGE is part of GL12
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

    // upload our ByteBuffer to GL
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA,
        GL_UNSIGNED_BYTE, buf);
  }

  public void bind() {
    glBindTexture(GL_TEXTURE_2D, id);
  }
}