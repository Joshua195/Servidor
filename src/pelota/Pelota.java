package pelota;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public class Pelota implements Serializable{

    private static Color[] colors = new Color[]{Color.BLACK,Color.BLUE,Color.DARK_GRAY,Color.RED,Color.GREEN};
    private Float x;
    private Float y;
    private Float vx;
    private Float vy;
    private Color color;
    private Integer puerto;

    public Pelota(Integer puerto) {
        x = 10f;
        y = 20f;
        vx = 50f;
        vy = 50f;
        //color = colors[new Random().nextInt(colors.length)];
        color = new Color(new Random().nextInt(255),new Random().nextInt(255),new Random().nextInt(255));
        this.puerto = puerto;
    }
    
    public Pelota(Float x, Float y, Integer puerto) {
        this.x = x;
        this.y = y;
        vx = 50f;
        vy = 50f;
        color = Color.BLACK;
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

        if(getPuerto().equals(pelota.getPuerto())){
            return true;
        }
        return false;
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

    public Float getVx() {
        return vx;
    }

    public void setVx(Float vx) {
        this.vx = vx;
    }

    public Float getVy() {
        return vy;
    }

    public void setVy(Float vy) {
        this.vy = vy;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Integer getPuerto() {
        return puerto;
    }

    public void setPuerto(Integer puerto) {
        this.puerto = puerto;
    }
}
