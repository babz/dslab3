package management;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class PricingCurve {
	
	private Map<Integer, Double> curveMappings = new TreeMap<Integer, Double>();
	private static PricingCurve instance = null;

	private PricingCurve() {
		curveMappings.put(0, 0.00);
	}
	
	public synchronized static PricingCurve getInstance() {
		if(instance == null) {
			instance = new PricingCurve();
		}
		return instance;
	}

	public synchronized String getCurve() {
		String curve = "Task count | Discount\n";
		for(Entry<Integer, Double> priceStep: curveMappings.entrySet()) {
			curve += priceStep.getKey() + " | " + priceStep.getValue() + " %\n";
		}
		return curve;
	}

	public synchronized void setOrAddPriceStep(int taskCount, double percent) {
		curveMappings.put(taskCount, percent);
	}
	
	/**
	 * 
	 * @param sumOfExecutedTask
	 * @return discount in percent
	 */
	public synchronized double getDiscount(int sumOfExecutedTasks) {
		double discount = 0;
		for(Entry<Integer, Double> priceStep: curveMappings.entrySet()) {
			if(priceStep.getKey() <= sumOfExecutedTasks) {
				System.out.println("using discount: " + priceStep.getKey() + " value: " + priceStep.getValue());
				discount = priceStep.getValue();
			}
		}
		return discount;
	}
}
