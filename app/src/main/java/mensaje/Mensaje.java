package mensaje;

import java.io.Serializable;

/**
 * Created by CAMILO on 08/10/2016.
 */
     public class Mensaje implements Serializable{

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private String orden;

        public Mensaje(String orden){
            this.orden=orden;

        }

        public String getOrden() {
            return orden;
        }

        public void setOrden(String orden) {
            this.orden = orden;
        }
}
