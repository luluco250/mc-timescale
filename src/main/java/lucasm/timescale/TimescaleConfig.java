package lucasm.timescale;

public final class TimescaleConfig {
	private int delay;
	private int stride;

	public float calculateTimeScale() {
		return (1f / delay) * stride;
	}

	/**
	 * @return How many ticks to wait before advancing the daytime cycle.
	 */
	public int getDelay() {
		return delay;
	}

	public void setDelay(int value) {
		delay = value;
	}

	/**
	 * @return How many ticks to add to the daytime when advancing.
	 */
	public int getStride() {
		return stride;
	}

	public void setStride(int stride) {
		this.stride = stride;
	}
}
