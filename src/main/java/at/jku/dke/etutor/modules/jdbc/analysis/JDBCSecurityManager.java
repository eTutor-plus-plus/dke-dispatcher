package at.jku.dke.etutor.modules.jdbc.analysis;

import java.io.FilePermission;
import java.lang.reflect.ReflectPermission;
import java.security.Permission;

/**
 * @author eiching
 *
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class JDBCSecurityManager extends SecurityManager {
    String libPath;
    String classesPath;

    public JDBCSecurityManager(String classesPath, String libPath) {
        super();
        this.libPath = libPath;
        this.classesPath = classesPath;
    }

    public void checkPermission(Permission perm) {
        boolean freeAccess = false;
        Class[] stack = this.getClassContext();
        Class[] interfaces = null;
        int i=0;
        boolean found = false;
        while ((i<stack.length) && !found) {
            interfaces = stack[i].getInterfaces();
            for (int j=0;j<interfaces.length;j++) {
                if (interfaces[j].getName().equalsIgnoreCase("etutor.modules.jdbc.analysis.JDBCExecutor")) {
                    //System.out.println("Permission requested by JDBCRunner: " + perm);
                    freeAccess = (isValidFilePermission(perm) || isValidReflectPermission(perm) ||
                            isValidPropertyPermission(perm));
                    found = true;
                }
            }
            i++;
        }
        if (!freeAccess && found) {
            throw new SecurityException("Requested permission " + perm + " not granted!");
            //super.checkPermission(perm);
        }
    }

    private boolean isValidFilePermission(Permission perm) {
        if (perm instanceof FilePermission) {
            System.out.println("Checking File permission against paths: " + classesPath + " and " + libPath);
            if (perm.getActions().equalsIgnoreCase("read") &&
                    ((perm.getName().indexOf(classesPath)!=-1) ||
                            (perm.getName().indexOf(classesPath.replaceAll("/","\\\\"))!=-1) ||
                            (perm.getName().indexOf(libPath)!=-1) ||
                            (perm.getName().indexOf(libPath.replaceAll("/","\\\\"))!=-1)))  {
                return true;
            }

            if (perm.getActions().equalsIgnoreCase("read") && ((perm.getName().indexOf("JDBCExecutor.class")!=-1))) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidPropertyPermission(Permission perm) {
        if (perm instanceof java.util.PropertyPermission) {
			/*
			if (perm.getActions().equalsIgnoreCase("read") &&
				(perm.getName().indexOf("file.encoding")!=-1)) {
				return true;
			}
			if (perm.getActions().equalsIgnoreCase("read") &&
				(perm.getName().indexOf("line.separator")!=-1)) {
				return true;
			}*/

            return (perm.getActions().equalsIgnoreCase("read"));

        }
        return false;
    }

    private boolean isValidReflectPermission(Permission perm) {
        if (perm instanceof ReflectPermission) {
            if (perm.getName().indexOf("suppressAccessChecks")!=-1) {
                return true;
            }
        }
        return false;
    }

}
