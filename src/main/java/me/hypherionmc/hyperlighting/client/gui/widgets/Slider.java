package me.hypherionmc.hyperlighting.client.gui.widgets;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class Slider extends SliderWidget {

    private final double maxValue;
    private final ISliderChanged sliderChanged;

    public Slider(int x, int y, int width, int height, Text text, double value, double maxValue, ISliderChanged sliderChanged) {
        super(x, y, width, height, text, value / maxValue);
        this.maxValue = maxValue;
        this.sliderChanged = sliderChanged;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(getDisplayString());
    }

    @Override
    protected void applyValue() {
        this.sliderChanged.onSliderChange(this);
    }

    private Text getDisplayString() {
        long seconds = Math.round(this.value * this.maxValue / 20);
        long minutes = Math.round(seconds / 60);
        if (this.value * this.maxValue >= 1200) {
            String appendString = (minutes == 1) ? "Minute" : "Minutes";
            String doSeconds = ((seconds - (minutes * 60)) > 0) ? ", " + (seconds - (minutes * 60)) + " Seconds" : "";
            return new LiteralText(minutes + " " + appendString + doSeconds);
        } else {
            return new LiteralText(seconds + " Seconds");
        }
    }

    public double getValue() {
        return this.value;
    }

    public interface ISliderChanged {
        void onSliderChange(Slider slider);
    }
}
