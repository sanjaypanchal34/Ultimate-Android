package in.appsaint.communication;

import java.io.File;
import java.io.Serializable;

public class PART implements Serializable {
        private String paramKey;
        private File file;

    public PART(String paramKey, File file) {
        this.paramKey = paramKey;
        this.file = file;
    }

    String getParamKey() {
            return paramKey;
        }

        public void setParamKey(String paramKey) {
            this.paramKey = paramKey;
        }

        File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }
    }