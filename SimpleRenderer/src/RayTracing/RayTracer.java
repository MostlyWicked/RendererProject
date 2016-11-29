package RayTracing;

import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.imageio.ImageIO;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {

    public int imageWidth;
    public int imageHeight;
    public Camera camera;
    public Scene scene;

    public RayTracer()
    {
        camera = new Camera();
        scene = new Scene();
    }

    /**
     * Runs the ray tracer. Takes scene file, output image file and image size as input.
     */
    public static void main(String[] args) {

        try {

            RayTracer tracer = new RayTracer();

            // Default values:
            tracer.imageWidth = 500;
            tracer.imageHeight = 500;

            if (args.length < 2)
                throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

            String sceneFileName = args[0];
            String outputFileName = args[1];

            if (args.length > 3)
            {
                tracer.imageWidth = Integer.parseInt(args[2]);
                tracer.imageHeight = Integer.parseInt(args[3]);
            }

            // Parse scene file:
            tracer.parseScene(sceneFileName);

            // Render scene:
            tracer.renderScene(outputFileName);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (RayTracerException e) {
            System.out.println(e.getMessage());
        } /*catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
    }

    /**
     * Parses the scene file and creates the scene.
     */
    public void parseScene(String sceneFileName) throws IOException, RayTracerException
    {
        FileReader fr = new FileReader(sceneFileName);

        BufferedReader r = new BufferedReader(fr);
        String line = null;
        int lineNum = 0;
        System.out.println("Started parsing scene file " + sceneFileName);

        ArrayList<Material> materials = new ArrayList<Material>();


        while ((line = r.readLine()) != null)
        {
            line = line.trim();
            ++lineNum;

            if (line.isEmpty() || (line.charAt(0) == '#'))
            {  // This line in the scene file is a comment
                continue;
            }
            else
            {
                String code = line.substring(0, 3).toLowerCase();
                // Split according to white space characters:
                String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

                if (code.equals("cam"))
                {
                    camera.setPosition(params[0], params[1], params[2]);
                    camera.setLookAtPoint(params[3], params[4], params[5]);
                    camera.setUpVector(params[6], params[7], params[8]);
                    camera.setScreenDistance(params[9]);
                    camera.setScreenWidth(params[10]);

                    System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
                }
                else if (code.equals("set"))
                {
                    scene.setBackground(params[0], params[1], params[2]);
                    scene.setNumShadowRays(params[3]);
                    scene.setRecursionLevel(params[4]);

                    System.out.println(String.format("Parsed general settings (line %d)", lineNum));
                }
                else if (code.equals("mtl"))
                {
                    Material material = new Material();
                    material.setDiffuse(params[0], params[1], params[2]);
                    material.setSpecular(params[3], params[4], params[5]);
                    material.setReflection(params[6], params[7], params[8]);
                    material.setPhongCoefficient(params[9]);
                    material.setTransparency(params[10]);

                    materials.add(material);

                    System.out.println(String.format("Parsed material (line %d)", lineNum));
                }
                else if (code.equals("sph"))
                {
                    Sphere sphere = new Sphere();
                    sphere.setCenter(params[0], params[1], params[2]);
                    sphere.setRadius(params[3]);

                    if(params.length < 5)
                    {
                        sphere.setMaterial(materials.get(0));
                    }
                    else
                    {
                        sphere.setMaterial(materials.get(Integer.parseInt(params[4]) - 1));
                    }

                    scene.addSurface(sphere);

                    System.out.println(String.format("Parsed sphere (line %d)", lineNum));
                }
                else if (code.equals("pln"))
                {
                    Plane plane = new Plane();
                    plane.setNormal(params[0], params[1], params[2]);
                    plane.setOffset(params[3]);

                    if(params.length < 5)
                    {
                        plane.setMaterial(materials.get(0));
                    }
                    else
                    {
                        plane.setMaterial(materials.get(Integer.parseInt(params[4]) - 1));
                    }

                    scene.addSurface(plane);

                    System.out.println(String.format("Parsed plane (line %d)", lineNum));
                }
                else if (code.equals("box"))
                {
                    Box box = new Box();
                    box.setCenter(params[0], params[1], params[2]);
                    box.setScale(params[3], params[4], params[5]);
                    box.setRotation(params[6], params[7], params[8]);

                    if(params.length < 10)
                    {
                        box.setMaterial(materials.get(0));
                    }
                    else
                    {
                        box.setMaterial(materials.get(Integer.parseInt(params[9]) - 1));
                    }

                    scene.addSurface(box);

                    System.out.println(String.format("Parsed box (line %d)", lineNum));
                }
                else if (code.equals("lgt"))
                {
                    Light light = new Light();
                    light.setPosition(params[0], params[1], params[2]);
                    light.setColor(params[3], params[4], params[5]);
                    light.setSpecularIntensity(params[6]);
                    light.setShadowIntensity(params[7]);
                    light.setLightRadius(params[8]);

                    scene.addLight(light);

                    System.out.println(String.format("Parsed light (line %d)", lineNum));
                }
                else
                {
                    System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
                }
            }
        }

        // It is recommended that you check here that the scene is valid,
        // for example camera settings and all necessary materials were defined.

        System.out.println("Finished parsing scene file " + sceneFileName);

    }

    /**
     * Renders the loaded scene and saves it to the specified file location.
     */
    public void renderScene(String outputFileName)
    {
        long startTime = System.currentTimeMillis();

        // Create a byte array to hold the pixel data:
        byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];


        for(int i = 0; i < this.imageWidth; i++) {
            for (int j = 0; j < this.imageHeight; j++) {
                Ray ray = constructRayThroughPixel(this.camera, i, j);
                TreeMap<Intersection, Surface> intersections = findIntersections(ray, this.scene);
                CustomColor pixelColor = getColor(this.scene, intersections, ray);
                UpdateRgbData(rgbData, pixelColor, i , j);
            }
        }

        long endTime = System.currentTimeMillis();
        Long renderTime = endTime - startTime;

        // The time is measured for your own convenience, rendering speed will not affect your score
        // unless it is exceptionally slow (more than a couple of minutes)
        System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

        // This is already implemented, and should work without adding any code.
        saveImage(this.imageWidth, rgbData, outputFileName);

        System.out.println("Saved file " + outputFileName);
    }

    public Ray constructRayThroughPixel(Camera camera, int i, int j)
    {
        float screenWidth = camera.getScreenWidth();
        float screenHeight = screenWidth/this.imageWidth*this.imageHeight;

        Vector directionVector = camera.getDirection();
        Vector upVector = camera.getUpVector(directionVector).multiply(-1);
        Vector sideVector = directionVector.crossProduct(upVector);
        Vector screenCenter = camera.getPosition().add(directionVector.multiply(camera.getScreenDistance()));
        Vector screenOrigin = screenCenter.add(upVector.multiply(-screenHeight/2)).add(sideVector.multiply(-screenWidth/2));

        Vector pixelPosition = screenOrigin.add(sideVector.multiply((float) (((float)i + 0.5)/this.imageWidth )*screenWidth)).add(upVector.multiply((float) (((float) j + 0.5) / this.imageHeight) * screenHeight));
        Vector rayDirection = (pixelPosition.subtract(camera.getPosition())).normalize();

        return new Ray(camera.getPosition(), rayDirection);
    }

    public TreeMap<Intersection, Surface> findIntersections(Ray ray, Scene scene)
    {
        TreeMap<Intersection, Surface> intersections = new TreeMap<Intersection, Surface>();
        Intersection singleIntersection;

        for(Surface surface : scene.getSurfaces())
        {
            singleIntersection = surface.findIntersection(ray);
            if(singleIntersection != null)
            {
                intersections.put(singleIntersection, surface);
            }
        }
        return intersections;
    }

    private TreeMap<Intersection,Surface> copyOfMapWithoutFirstEntry(TreeMap<Intersection,Surface> t){
        TreeMap<Intersection,Surface> u = new TreeMap<Intersection,Surface>();
        for(Entry<Intersection,Surface> val: t.entrySet()){
            if(val.equals(t.firstEntry()))
                continue;
            u.put(val.getKey(), val.getValue());
        }
        return u;
    }

    public CustomColor getColor(Scene scene, TreeMap<Intersection, Surface> intersections, Ray ray){
        return getColor(scene,intersections,ray,scene.getRecursionLevel());
    }

    public CustomColor getColor(Scene scene, TreeMap<Intersection, Surface> intersections, Ray ray, int recursionParameter)
    {
        if(intersections.isEmpty())
        {
            return scene.getBackground();
        }

        Surface firstSurface = intersections.firstEntry().getValue();
        Intersection firstIntersection = intersections.firstEntry().getKey();
        Material firstMaterial = firstSurface.getMaterial();

        CustomColor basicSurfaceColor = firstSurface.getColor(scene, firstIntersection, ray);
        CustomColor finalColor = new CustomColor(Color.black);

        double transpValue = firstMaterial.getTransparency();

        //Transparency
        if (firstMaterial.getTransparency() > 0){

            CustomColor backgroundColor = new CustomColor(0,0,0);

            backgroundColor = backgroundColor.add(getColor(scene,copyOfMapWithoutFirstEntry(intersections),ray,recursionParameter));

            finalColor = backgroundColor.multiply(transpValue);
        }

        finalColor = finalColor.add(basicSurfaceColor.multiply(1 - transpValue));

        //Reflections
        if (recursionParameter > 0 && !firstMaterial.getReflectionColor().getRgb().isEqual(new Vector(0,0,0))){

            Ray reflectionRay = getReflectionRay(ray, firstIntersection);
            TreeMap<Intersection,Surface> reflectionIntersections = findIntersections(reflectionRay, scene);

            if(reflectionIntersections.isEmpty()){
                finalColor = finalColor.add(scene.getBackground().multiply(firstMaterial.getReflectionColor()));
            }

            else{
                finalColor = finalColor.add(getColor(scene, reflectionIntersections, reflectionRay, recursionParameter - 1).multiply(firstMaterial.getReflectionColor()));
            }
        }

        return finalColor.clip();
    }

    private Ray getReflectionRay(Ray ray, Intersection intersection){
        Vector V = ray.getDirection();
        Vector N = intersection.getNormal();

        Vector reflectionDirection = V.subtract(N.multiply(2 * V.dotProduct(N))).normalize();
        Vector intersectionPosition = getIntersectionPosition(ray, intersection);

        return new Ray(intersectionPosition, reflectionDirection);
    }


    public static Vector getIntersectionPosition(Ray ray, Intersection intersection){
        double epsilon = 0.01;
        return ray.getOrigin().add(ray.getDirection().multiply(intersection.getCoefficient() - epsilon));
    }

    public void UpdateRgbData(byte[] data, CustomColor pixelColor, int x, int y)
    {
        Color color = pixelColor.getJavaColor();

        data[(y * this.imageWidth + x) * 3] = (byte) color.getRed();
        data[(y * this.imageWidth + x) * 3 + 1] = (byte) color.getGreen();
        data[(y * this.imageWidth + x) * 3 + 2] = (byte) color.getBlue();
    }

    //////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

    /*
     * Saves RGB data as an image in png format to the specified location.
     */
    public static void saveImage(int width, byte[] rgbData, String fileName)
    {
        try {

            BufferedImage image = bytes2RGB(width, rgbData);
            ImageIO.write(image, "png", new File(fileName));

        } catch (IOException e) {
            System.out.println("ERROR SAVING FILE: " + e.getMessage());
        }
    }

    /*
     * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
     */
    public static BufferedImage bytes2RGB(int width, byte[] buffer) {
        int height = buffer.length / width / 3;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, false,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBufferByte db = new DataBufferByte(buffer, width * height);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        BufferedImage result = new BufferedImage(cm, raster, false, null);

        return result;
    }

    public static class RayTracerException extends Exception {
        public RayTracerException(String msg) {  super(msg); }
    }
}