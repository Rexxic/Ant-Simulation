package de.hhn.it.ui;

import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Rotate;

/**
 *
 */
final class AntModelGraphicBuilder {
    private final WritableImage frontLeftImage;
    private final WritableImage frontRightImage;
    private final WritableImage middleLeftImage;
    private final WritableImage middleRightImage;
    private final WritableImage rearLeftImage;
    private final WritableImage rearRightImage;
    private final WritableImage bodyAndAntannaImage;
    private final Paint foodColor;

    AntModelGraphicBuilder(Color legColor, Color antennaeColor, Color headColor, Color middleColor, Color rearColor, Color foodColor) {

        this.foodColor = foodColor;
        // Legs
        SVGPath legFrontLeftSvg = new SVGPath();
        legFrontLeftSvg.setFill(legColor);
        SVGPath legFrontRightSvg = new SVGPath();
        legFrontRightSvg.setFill(legColor);
        SVGPath legMiddleLeftSvg = new SVGPath();
        legMiddleLeftSvg.setFill(legColor);
        SVGPath legMiddleRightSvg = new SVGPath();
        legMiddleRightSvg.setFill(legColor);
        SVGPath legRearLeftSvg = new SVGPath();
        legRearLeftSvg.setFill(legColor);
        SVGPath legRearRightSvg = new SVGPath();
        legRearRightSvg.setFill(legColor);

        legFrontLeftSvg
                .setContent("M 14.054035,7.5292947 C 13.751773,7.5673977 13.398701,7.2803848 13.728399,7.0192933 14.001172,6.717137 14.331754,6.4700873 14.56434,6.1315495 15.045854,5.5564384 15.534064,4.9869978 16.009319,4.406567 17.315656,3.8639944 18.718953,3.6278371 20.057883,3.1892197 20.290206,3.1574808 20.609639,3.042731 20.804101,3.1370014 20.491548,3.2952555 20.136935,3.3536176 19.810147,3.4799288 18.707996,3.8438006 17.614066,4.2379182 16.53773,4.6713137 15.873573,5.0225634 15.537962,5.7374482 15.034002,6.2633763 14.73827,6.6261139 14.395506,6.9722234 14.234303,7.4207459 c -0.05506,0.058338 -0.09406,0.1080674 -0.180268,0.1085651 z");
        legFrontRightSvg
                .setContent("m 14.054035,9.9829874 c -0.302262,-0.0381 -0.655334,0.2489096 -0.325636,0.5100016 0.272773,0.302156 0.603355,0.549206 0.835941,0.887743 0.481514,0.575111 0.969724,1.144552 1.444979,1.724983 1.306337,0.542572 2.709634,0.77873 4.048564,1.217347 0.232323,0.03174 0.551756,0.146489 0.746218,0.05222 C 20.491548,14.217028 20.136935,14.158666 19.810147,14.032355 18.707996,13.668483 17.614066,13.274366 16.53773,12.84097 15.873573,12.48972 15.537962,11.774836 15.034002,11.248908 14.73827,10.88617 14.395506,10.54006 14.234303,10.091538 14.179243,10.033198 14.140243,9.9834714 14.054035,9.9829734 z");
        legMiddleLeftSvg
                .setContent("M 13.432291,7.2852586 C 13.376041,7.568101 12.894798,7.7938525 12.708167,7.438016 12.418958,7.080071 12.36456,6.6561572 13.09238,6.2509475 12.059595,5.2539768 11.660335,4.2575181 11.788932,3.2344867 11.849242,2.1565629 11.754232,1.0705973 11.971323,0 c 0.211871,0.3081654 0.08969,0.6728722 0.106906,1.0051707 0.0119,0.4226412 0.08188,0.8433624 0.08995,1.2668846 0.04732,0.9330124 0.121943,1.8714237 0.411945,2.782178 0.121578,0.589758 0.243157,1.1795236 0.364736,1.7692817 0.198051,0.118301 0.535306,0.2320238 0.487432,0.4617436 z");
        legMiddleRightSvg
                .setContent("m 13.432291,10.220459 c -0.05625,-0.2828416 -0.537493,-0.5085936 -0.724124,-0.152757 -0.289209,0.357945 -0.343607,0.781859 -0.415787,1.187068 -0.232785,0.996971 -0.632045,1.99343 -0.503448,3.016461 0.06031,1.077924 -0.0347,2.16389 0.182391,3.234487 0.211871,-0.308166 0.08969,-0.672872 0.106906,-1.005171 0.0119,-0.422641 0.08188,-0.843362 0.08995,-1.266884 0.04732,-0.933013 0.121943,-1.871424 0.411945,-2.782178 0.121578,-0.589758 0.243157,-1.179524 0.364736,-1.769282 0.198051,-0.118301 0.535306,-0.232024 0.487432,-0.461744 z");
        legRearLeftSvg
                .setContent("m 10.837002,8.0873048 c 0.401692,0.11957 1.020849,-0.19688 0.671217,-0.65363 -0.23855,-0.40536 -0.76074,-0.43244 -1.027163,-0.80564 C 9.640899,5.8169848 8.793339,5.0135448 7.962582,4.1928148 6.119617,3.6612548 4.202746,3.4744048 2.3229242,3.1208248 1.5562041,3.0005148 0.7819209,2.7479548 0,2.8914848 c 1.1019082,0.30664 3.0428196,0.44586 3.3414,0.76798 1.459797,0.35914 2.975876,0.58405 4.350962,1.21105 0.863858,0.85473 1.727716,1.70946 2.591574,2.56419 0.07859,0.26905 0.210626,0.65974 0.553066,0.6526 z");
        legRearRightSvg
                .setContent("m 10.837002,9.398985 c 0.401692,-0.11957 1.020849,0.19688 0.671217,0.653629 -0.23855,0.40536 -0.76074,0.43244 -1.027163,0.80564 -0.8401571,0.81105 -1.6877171,1.61449 -2.5184741,2.43522 C 6.1196169,13.825033 4.202746,14.011883 2.3229242,14.365463 1.5562041,14.485773 0.7819209,14.738333 0,14.594803 c 1.1019082,-0.30664 3.0428196,-0.44586 3.3414,-0.76798 1.459797,-0.359139 2.9758759,-0.584049 4.3509619,-1.211049 0.863858,-0.85473 1.727716,-1.70946 2.5915741,-2.56419 0.07859,-0.269049 0.210626,-0.659739 0.553066,-0.652599 z");

        SnapshotParameters snapshotParameter = new SnapshotParameters();
        snapshotParameter.setFill(Color.TRANSPARENT);

        frontLeftImage = legFrontLeftSvg.snapshot(snapshotParameter, null);
        frontRightImage = legFrontRightSvg.snapshot(snapshotParameter, null);
        middleLeftImage = legMiddleLeftSvg.snapshot(snapshotParameter, null);
        middleRightImage = legMiddleRightSvg.snapshot(snapshotParameter, null);
        rearLeftImage = legRearLeftSvg.snapshot(snapshotParameter, null);
        rearRightImage = legRearRightSvg.snapshot(snapshotParameter, null);


        // Body
        Canvas bodyAndAntannae = new Canvas(23.59, 17.51);
        GraphicsContext bodyAndAntannaeGraphicsContext = bodyAndAntannae.getGraphicsContext2D();

        // antanna left
        bodyAndAntannaeGraphicsContext.beginPath();
        bodyAndAntannaeGraphicsContext
                .appendSVGPath("M 19.228053,8.4090564 C 19.38738,7.6058879 19.780879,7.0456857 20.205715,6.7672714 20.656802,6.4495976 21.141474,6.3829624 21.615301,6.264022 22.109153,6.1106021 22.64059,5.7700677 22.92682,4.9566763 22.97986,4.8047343 23.02229,4.6402867 23.053655,4.4689901");
        bodyAndAntannaeGraphicsContext.setFill(antennaeColor);
        bodyAndAntannaeGraphicsContext.fill();
        // antanna right
        bodyAndAntannaeGraphicsContext.beginPath();
        bodyAndAntannaeGraphicsContext
                .appendSVGPath("m 19.167153,9.2572784 c 0.261894,0.722637 0.726641,1.1044586 1.186651,1.1949866 0.491253,0.117855 0.983948,-0.02622 1.472498,-0.114097 0.512965,-0.06256 1.087413,0.04305 1.477386,0.72085 0.07241,0.126806 0.135835,0.270522 0.189082,0.42577");
        bodyAndAntannaeGraphicsContext.setFill(antennaeColor);
        bodyAndAntannaeGraphicsContext.fill();

        // head
        bodyAndAntannaeGraphicsContext.beginPath();
        bodyAndAntannaeGraphicsContext
                .appendSVGPath("m 19.902864,8.8063284 c 0.04548,0.84878 -0.506364,1.6409496 -1.24441,2.0212096 -0.716207,0.39216 -1.662537,0.41122 -2.335497,-0.084 -0.597898,-0.34086 -0.980067,-0.9917696 -1.040427,-1.6712796 -0.100878,-0.909 0.327325,-1.90497 1.165991,-2.32647 0.511614,-0.2798 1.126677,-0.36862 1.691022,-0.21025 0.925657,0.23655 1.699314,1.06838 1.759238,2.03855 0.0055,0.0773 0.0069,0.15482 0.0041,0.23223 z");
        bodyAndAntannaeGraphicsContext.setFill(headColor);
        bodyAndAntannaeGraphicsContext.fill();
        // middle
        bodyAndAntannaeGraphicsContext.beginPath();
        bodyAndAntannaeGraphicsContext
                .appendSVGPath("m 15.356974,8.7697684 c -0.04322,0.68176 -0.489288,1.2906796 -1.084407,1.6062796 -0.896,0.48529 -2.025693,0.51394 -2.946637,0.0794 -0.621149,-0.18836 -1.093913,-0.7573096 -1.174749,-1.3998596 -0.127855,-0.80284 0.29804,-1.68611 1.060816,-2.00809 0.239343,-0.0965 0.491701,-0.15369 0.739177,-0.22538 0.898098,-0.1933 1.898144,-0.0394 2.643222,0.51622 0.449729,0.33571 0.745225,0.8671 0.762578,1.43147 z");
        bodyAndAntannaeGraphicsContext.setFill(middleColor);
        bodyAndAntannaeGraphicsContext.fill();
        // rear
        bodyAndAntannaeGraphicsContext.beginPath();
        bodyAndAntannaeGraphicsContext
                .appendSVGPath("m 10.257903,8.7272784 c -0.07915,0.90962 -0.839213,1.5790696 -1.639274,1.9060996 -1.227772,0.53353 -2.603772,0.54135 -3.9127673,0.40113 -1.3786917,-0.16539 -2.7685771,-0.66671 -3.7657535,-1.6643396 -0.3223778,-0.40363 -0.3391914,-1.01174 1.563e-4,-1.41041 0.5821678,-0.70805 1.4763004,-1.06044 2.3301463,-1.32167 1.6856431,-0.46016 3.5492122,-0.58833 5.1939232,0.0955 0.726094,0.30247 1.462741,0.78485 1.725962,1.56538 0.04438,0.13817 0.06829,0.28313 0.06761,0.42835 z");
        bodyAndAntannaeGraphicsContext.setFill(rearColor);
        bodyAndAntannaeGraphicsContext.fill();

        bodyAndAntannaImage = bodyAndAntannae.snapshot(snapshotParameter, null);
    }

    static String key(Color legColor, Color antennaeColor, Color headColor, Color middleColor, Color rearColor, Color foodColor) {
        StringBuilder b = new StringBuilder();
        b.append(legColor);
        b.append(antennaeColor);
        b.append(headColor);
        b.append(middleColor);
        b.append(rearColor);
        b.append(foodColor);
        return b.toString();
    }

    private ImageView buildImageView(WritableImage image, boolean cache, CacheHint cacheHint, double x, double y, Rotate rotate) {
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setCache(cache);
        imageView.setCacheHint(cacheHint);
        imageView.setX(x);
        imageView.setY(y);
        if (rotate != null) {
            imageView.getTransforms().add(rotate);
        }
        return imageView;
    }

    Node getFrontLeftLeg() {
        return buildImageView(frontLeftImage, !AntApplication.TURBO_MODE, CacheHint.ROTATE, 13.596, 3.1, new Rotate(0, AntModelGraphic.LEG_FRONT_LEFT_ROTATION_X, AntModelGraphic.LEG_FRONT_LEFT_ROTATION_Y));
    }

    Node getFrontRightLeg() {
        return buildImageView(frontRightImage, !AntApplication.TURBO_MODE, CacheHint.ROTATE, 13.596, 8.8, new Rotate(0, AntModelGraphic.LEG_FRONT_RIGHT_ROTATION_X, AntModelGraphic.LEG_FRONT_RIGHT_ROTATION_Y));
    }

    Node getMiddleLeftLeg() {
        return buildImageView(middleLeftImage, !AntApplication.TURBO_MODE, CacheHint.ROTATE, 11.764, 0, new Rotate(0, AntModelGraphic.LEG_MIDDLE_LEFT_ROTATION_X, AntModelGraphic.LEG_MIDDLE_LEFT_ROTATION_Y));
    }

    Node getMiddleRightLeg() {
        return buildImageView(middleRightImage, !AntApplication.TURBO_MODE, CacheHint.ROTATE, 11.764, 9.9, new Rotate(0, AntModelGraphic.LEG_MIDDLE_RIGHT_ROTATION_X, AntModelGraphic.LEG_MIDDLE_RIGHT_ROTATION_Y));
    }

    Node getRearLeftLeg() {
        return buildImageView(rearLeftImage, !AntApplication.TURBO_MODE, CacheHint.ROTATE, 0, 1.6, new Rotate(0, AntModelGraphic.LEG_REAR_LEFT_ROTATION_X, AntModelGraphic.LEG_REAR_LEFT_ROTATION_Y));
    }

    Node getRearRightLeg() {
        return buildImageView(rearRightImage, !AntApplication.TURBO_MODE, CacheHint.ROTATE, 0, 9.4, new Rotate(0, AntModelGraphic.LEG_REAR_RIGHT_ROTATION_X, AntModelGraphic.LEG_REAR_RIGHT_ROTATION_Y));
    }

    Node getBodyAndAntenna() {
        return buildImageView(bodyAndAntannaImage, !AntApplication.TURBO_MODE, CacheHint.ROTATE, 0, 0, null);
    }

    Node getFood() {
        Rectangle food = new Rectangle();

        food.setX(21);
        food.setY(8);
        food.setWidth(3);
        food.setHeight(1);
        food.setVisible(false);
        food.setFill(foodColor);

        return food;
    }
}