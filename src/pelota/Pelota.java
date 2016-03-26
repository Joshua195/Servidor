package pelota;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public class Pelota implements Serializable{

    private Float x;
    private Float y;
    private Float vx;
    private Float vy;
    private Color color;
    private Integer puerto;

    public Pelota(Integer puerto) {
        x = (float) new Random().nextInt(Ctes.ANCHO);
        y = (float) new Random().nextInt(Ctes.ALTO);
        vx = 50f;
        vy = 50f;
        color = new Color(new Random().nextInt(255),new Random().nextInt(255),new Random().nextInt(255));
        this.puerto = puerto;
    }
    
    public void fisica(float dt) {
        x += vx * dt;
        y += vy * dt;
        if (vx < 0 && x <= 0 || vx > 0 && x + Ctes.DIAMETRO >= Ctes.ANCHO) {
            vx = -vx;
        }
        if (vy < 0 && y < 0 || vy > 0 && y + Ctes.DIAMETRO >= Ctes.ALTO) {
            vy = -vy;
        }
    }

    @Override
    public boolean equals(Object object){
        if(object == null || !(object instanceof Pelota)){
            return false;
        }
        Pelota pelota = (Pelota)object;
        return getPuerto().equals(pelota.getPuerto());
    }

    @Override
    public int hashCode(){
        return this.getPuerto().hashCode();
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public Integer getPuerto() {
        return puerto;
    }
}
