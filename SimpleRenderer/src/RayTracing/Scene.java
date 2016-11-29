package RayTracing;

import java.awt.*;
import java.util.ArrayList;

public class Scene {

    private CustomColor _background;
    private int _numShadowRays;
    private int _recursionLevel;
    private ArrayList<Light> _lights;
    private ArrayList<Surface> _surfaces;


    public Scene() {
        _lights = new ArrayList<Light>();
        _surfaces = new ArrayList<Surface>();
    }

    public ArrayList<Surface> getSurfaces()
    {
        return _surfaces;
    }

    public ArrayList<Light> getLights()
    {
        return _lights;
    }

    public CustomColor getBackground()
    {
        return _background;
    }

    public int getNumShadowRays()
    {
        return _numShadowRays;
    }

    public int getRecursionLevel(){
        return _recursionLevel;
    }

    public void setBackground(CustomColor background)
    {
        _background = background;
    }

    public void setBackground(String r, String g, String b)
    {
        CustomColor background = new CustomColor(Float.parseFloat(r), Float.parseFloat(g), Float.parseFloat(b));
        setBackground(background);
    }

    public void setNumShadowRays(int numShadowRays)
    {
        _numShadowRays = numShadowRays;
    }

    public void setNumShadowRays(String numShadowRays)
    {
        setNumShadowRays(Integer.parseInt(numShadowRays));
    }

    public void setRecursionLevel(int recursionLevel)
    {
        _recursionLevel = recursionLevel;
    }

    public void setRecursionLevel(String recursionLevel)
    {
        setRecursionLevel(Integer.parseInt(recursionLevel));
    }

    public void addLight(Light light)
    {
        _lights.add(light);
    }

    public void addSurface(Surface surface)
    {
        _surfaces.add(surface);
    }
}