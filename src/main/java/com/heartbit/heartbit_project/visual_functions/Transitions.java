package com.heartbit.heartbit_project.visual_functions;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Transitions {

    public enum Direction {
        TO_LEFT, TO_RIGHT, TO_TOP, TO_BOTTOM;

        public Direction getOpposite() {
            switch (this) {
                case TO_LEFT: return TO_RIGHT;
                case TO_RIGHT: return TO_LEFT;
                case TO_TOP: return TO_BOTTOM;
                case TO_BOTTOM: return TO_TOP;
                default: throw new IllegalStateException("Unexpected direction: " + this);
            }
        }

        public void TranslateTo(Node node, double distance) {
            switch (this) {
                case TO_LEFT:
                    node.setTranslateX(distance);
                    break;
                case TO_RIGHT:
                    node.setTranslateX(-distance);
                    break;
                case TO_TOP:
                    node.setTranslateY(distance);
                    break;
                case TO_BOTTOM:
                    node.setTranslateY(-distance);
                    break;
            }
        }

        public void LocateTo(TranslateTransition node, double distance) {
            switch (this) {
                case TO_LEFT:
                    node.setByX(distance);
                    break;
                case TO_RIGHT:
                    node.setByX(-distance);
                    break;
                case TO_TOP:
                    node.setByY(distance);
                    break;
                case TO_BOTTOM:
                    node.setByY(-distance);
                    break;
            }
        }
    }


    public static void FadeOutIn(Node outNode, Node inNode, double durationMillis, Direction direction, double distance) {
        // Fade out transition
        FadeTransition fadeOut = new FadeTransition(Duration.millis(durationMillis), outNode);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        TranslateTransition translateOut = new TranslateTransition(Duration.millis(durationMillis), outNode);
        direction.LocateTo(translateOut, distance);

        ParallelTransition fadeOutTransition = new ParallelTransition(fadeOut, translateOut);

        // On fadeOut complete, hide outNode and fade in inNode
        fadeOutTransition.setOnFinished(event -> {
            outNode.setVisible(false);
            outNode.setManaged(false);

            FadeIn(inNode, durationMillis, direction, distance);
        });

        fadeOutTransition.play();
    }


    public static void FadeIn(Node item, double durationMillis, Direction direction, double distance) {

        direction.TranslateTo(item,distance);
        // Set initial opacity
        item.setOpacity(0.0);
        item.setVisible(true);
        item.setManaged(true);

        // Create fade transition
        FadeTransition fadeIn = new FadeTransition(Duration.millis(durationMillis), item);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Create translate transition if needed
        TranslateTransition translate = new TranslateTransition(Duration.millis(durationMillis), item);
        translate.setToX(0);
        translate.setToY(0);

        // Run both transitions in parallel
        ParallelTransition transition = new ParallelTransition(fadeIn, translate);
        transition.play();
    }

    public static void FadeOut(Node item, double durationMillis, Direction direction, double distance) {
        // Create fade transition
        FadeTransition fadeOut = new FadeTransition(Duration.millis(durationMillis), item);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // Create translate transition based on direction
        TranslateTransition translate = new TranslateTransition(Duration.millis(durationMillis), item);
        direction.LocateTo(translate,distance);

        // Run both transitions in parallel
        ParallelTransition transition = new ParallelTransition(fadeOut, translate);

        // Optional: hide the node after the animation
        transition.setOnFinished(event -> {
            item.setVisible(false);
            item.setManaged(false);
        });

        transition.play();
    }



}
