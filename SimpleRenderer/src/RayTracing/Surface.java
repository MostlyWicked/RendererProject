package RayTracing;


import java.awt.*;
import java.util.Random;

public abstract class Surface {

    private Material _material;

    public abstract Intersection findIntersection(Ray ray);

    public void setMaterial(Material material)
    {
        _material = material;
    }


    public CustomColor getColor(Scene scene, Intersection intersection, Ray ray)
    {
        Vector intersectionPosition = RayTracer.getIntersectionPosition(ray, intersection); //new static method in RayTracer class
        CustomColor diffuseMaterialColor = _material.getDiffuseColor();
        CustomColor specularMaterialColor = _material.getSpecularColor();
        CustomColor finalColor = new CustomColor(Color.black);

        for(Light light : scene.getLights()){
            Vector lightDirection = light.getPosition().subtract(intersectionPosition).normalize();
            Vector normal = intersection.getNormal();
            Vector reflectionVector = normal.multiply(2*normal.dotProduct(lightDirection)).subtract(lightDirection).normalize();
            double reflectionViewerDotProduct = Math.max(reflectionVector.dotProduct(ray.getDirection().normalize().multiply(-1)), 0);
            double specularIntensity = light.getSpecularIntensity()*Math.pow(reflectionViewerDotProduct, _material.getPhongCoefficient());
            double diffuseIntensity = Math.max(normal.dotProduct(lightDirection), 0);

            double softShadow = getSoftShadow(scene, intersectionPosition, light.getPosition(), light.getLightRadius());

            specularIntensity *= 1 - light.getShadowIntensity()*softShadow;
            diffuseIntensity *= 1 - light.getShadowIntensity()*softShadow;


            finalColor = finalColor.add(light.getColor().multiply(diffuseMaterialColor.multiply(diffuseIntensity).add(specularMaterialColor.multiply(specularIntensity))));
        }
        return finalColor.clip();
    }

    private double getTransparentShadow(Scene scene, Vector intersectionPosition, Vector lightPosition)
    {
        Vector lightDirection = lightPosition.subtract(intersectionPosition).normalize();
        double lightDistance = lightPosition.subtract(intersectionPosition).norm();
        Ray lightRay = new Ray(intersectionPosition, lightDirection);
        Intersection intersection = null;
        double transparencyValue = 1;

        for(Surface surface: scene.getSurfaces())
        {
            if(!surface.equals(this) && (intersection = surface.findIntersection(lightRay)) != null)//surface is not the current one and crosses the ray
            {
                if(intersection.getCoefficient() < lightDistance)//surface is between object and light source
                {
                    transparencyValue *= surface.getMaterial().getTransparency();
                    if(transparencyValue == 0)
                    {
                        break; //surface is opaque
                    }
                }
            }
        }
        return 1 - transparencyValue;
    }

    public Material getMaterial(){
        return _material;
    }

    private double getSoftShadow(Scene scene, Vector intersectionPosition, Vector lightPosition, float lightRadius)
    {
        Vector lightPlaneNormal = intersectionPosition.subtract(lightPosition).normalize();
        Vector XVector = new Vector(1, 0, 0);
        if(Math.abs(XVector.dotProduct(lightPlaneNormal)) == 1)
        {
            XVector = new Vector(0, 1, 0);
        }
        // Projection of the x-vector onto the plane normal to the normal vector
        Vector lightPlaneXDirection = XVector.subtract(lightPlaneNormal.multiply(XVector.dotProduct(lightPlaneNormal))).normalize();
        Vector lightPlaneYDirection = lightPlaneNormal.crossProduct(lightPlaneXDirection);

        double counter = 0.0;
        Random random = new Random();
        double cubeSize = lightRadius/scene.getNumShadowRays();

        for (int xx = 0; xx < scene.getNumShadowRays(); xx++)
        {
            for (int yy = 0; yy < scene.getNumShadowRays(); yy++)
            {
                Vector xOffset = lightPlaneXDirection.multiply(-lightRadius/2 + xx*cubeSize + random.nextDouble()*cubeSize);
                Vector yOffset = lightPlaneYDirection.multiply(-lightRadius/2 + yy*cubeSize + random.nextDouble()*cubeSize);

                Vector softLightRandomPosition = lightPosition.add(xOffset).add(yOffset);

                counter += getTransparentShadow(scene,intersectionPosition,softLightRandomPosition);

            }
        }
        return (counter)/(scene.getNumShadowRays()*scene.getNumShadowRays());
    }
}