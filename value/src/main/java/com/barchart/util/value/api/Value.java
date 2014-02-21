package com.barchart.util.value.api;


public final class Value {
	
	private Value() {
		
	}
	
	public static final Price min(final Price... vals) {
		
		Price res = Price.NULL;
		
		if(vals == null) {
			return res;
		}
		
		for(final Price p : vals) {
			
			if(p != null && !p.isNull()) {
				if(res.isNull() || p.lessThan(res)) {
					res = p;
				} 
			}
			
		}
		
		return res;
	}
	
	public static final Price max(final Price... vals) {
		
		Price res = Price.NULL;
		
		if(vals == null) {
			return res;
		}
		
		for(final Price p : vals) {
			
			if(p != null && !p.isNull()) {
				if(res.isNull() || p.greaterThan(res)) {
					res = p;
				} 
			}
			
		}
		
		return res;
	}
	
	public static final Size min(final Size... vals) {
		
		Size res = Size.NULL;
		
		if(vals == null) {
			return res;
		}
		
		for(final Size p : vals) {
			
			if(p != null && !p.isNull()) {
				if(res.isNull() || p.lessThan(res)) {
					res = p;
				} 
			}
			
		}
		
		return res;
	}
	
	public static final Size max(final Size... vals) {
		
		Size res = Size.NULL;
		
		if(vals == null) {
			return res;
		}
		
		for(final Size p : vals) {
			
			if(p != null && !p.isNull()) {
				if(res.isNull() || p.greaterThan(res)) {
					res = p;
				} 
			}
			
		}
		
		return res;
	}
	
}
