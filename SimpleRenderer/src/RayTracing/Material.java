package RayTracing;

public class Material {

    private CustomColor _diffuse;
    private CustomColor _specular;
    private CustomColor _reflection;
    private float _phongCoefficient;
    private float _transparency;

    public CustomColor getDiffuseColor()
    {
        return new CustomColor(_diffuse);
    }

    public CustomColor getSpecularColor()
    {
        return new CustomColor(_specular);
    }

    public CustomColor getReflectionColor()
    {
        return new CustomColor(_reflection);
    }

    public float getTransparency()
    {
        return this._transparency;
    }

    public float getPhongCoefficient()
    {
        return _phongCoefficient;
    }

    public void setDiffuse(CustomColor diffuse)
    {
        _diffuse = diffuse;
    }

    public void setDiffuse(String r, String g, String b)
    {
        CustomColor diffuse = new CustomColor(Float.parseFloat(r), Float.parseFloat(g), Float.parseFloat(b));
        setDiffuse(diffuse);
    }

    public void setSpecular(CustomColor specular)
    {
        _specular = specular;
    }

    public void setSpecular(String r, String g, String b)
    {
        CustomColor specular = new CustomColor(Float.parseFloat(r), Float.parseFloat(g), Float.parseFloat(b));
        setSpecular(specular);
    }

    public void setReflection(CustomColor reflection)
    {
        _reflection = reflection;
    }

    public void setReflection(String r, String g, String b)
    {
        CustomColor reflection = new CustomColor(Float.parseFloat(r), Float.parseFloat(g), Float.parseFloat(b));
        setReflection(reflection);
    }

    public void setPhongCoefficient(float phongCoefficient)
    {
        _phongCoefficient = phongCoefficient;
    }

    public void setPhongCoefficient(String phongCoefficient)
    {
        setPhongCoefficient(Float.parseFloat(phongCoefficient));
    }

    public void setTransparency(float transparency)
    {
        _transparency = transparency;
    }

    public void setTransparency(String transparency)
    {
        setTransparency(Float.parseFloat(transparency));
    }

}
