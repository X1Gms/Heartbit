package com.heartbit.heartbit_project.visual_functions;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Transitions {

    public enum Direction{
        TO_LEFT, TO_RIGHT, TO_TOP, TO_BOTTOM;

        public void TranslateTo(Node node,double distance){
            // Set initial position based on direction
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

        public void LocateTo(TranslateTransition node,double distance){
            // Set initial position based on direction
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

    public static void FadeOutIn(Node currentNode, Node nextNode, double durationMillis, double distanceX) {
        // Fade out current node
        FadeTransition fadeOut = new FadeTransition(Duration.millis(durationMillis), currentNode);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        TranslateTransition slideLeft = new TranslateTransition(Duration.millis(durationMillis), currentNode);
        slideLeft.setByX(-distanceX);

        ParallelTransition hideCurrent = new ParallelTransition(fadeOut, slideLeft);
        hideCurrent.setOnFinished(e -> {
            currentNode.setVisible(false); // Hide current node
            currentNode.setManaged(false);

            // Prepare next node: start slightly to the left and invisible
            nextNode.setTranslateX(-distanceX);
            nextNode.setOpacity(0.0);
            nextNode.setVisible(true);
            nextNode.setManaged(true);

            // Fade in and move next node to the right
            FadeTransition fadeIn = new FadeTransition(Duration.millis(durationMillis), nextNode);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            TranslateTransition slideRight = new TranslateTransition(Duration.millis(durationMillis), nextNode);
            slideRight.setToX(0);  // back to natural position

            ParallelTransition showNext = new ParallelTransition(fadeIn, slideRight);
            showNext.play();
        });

        hideCurrent.play();
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
