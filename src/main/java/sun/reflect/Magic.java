package sun.reflect;

/**
 * Any direct and indirect sub-classes will be loaded without any byte-code verification by JVM.
 * <br/>Created by a.hofmann on 28.05.2015.<br/>
 */
@SuppressWarnings("sunapi")
public class Magic extends MagicAccessorImpl {
   public Magic() {}
}
