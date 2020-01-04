/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static java.lang.Integer.parseInt;
import static java.lang.Math.min;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.InterfaceNameAdapter;

@JsonbTypeAdapter(InterfaceNameAdapter.class)
public class InterfaceName extends Scalar<String>{

	private static final long serialVersionUID = 1L;
	
	public static InterfaceName interfaceName(String name) {
		return valueOf(name);
	}

	
	public static InterfaceName valueOf(String name) {
		return fromString(name,InterfaceName::new);
	}

	@NotNull(message="{interface_name.required}")
	@Pattern(regexp="[a-z0-9]{2,4}-?(\\d+/)+\\d+(:\\d+)?", message="{interface_name.invalid}")
	private String value;
	
	public InterfaceName(String value){
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}

	@Override
	public int compareTo(Scalar<String> name) {
		InterfaceName ifname = (InterfaceName) (name);
		String[]	 a = getValue().split("[:/\\-]");
		String[] b = ifname.getValue().split("[:/\\-]");
		int l = min(a.length, b.length);
		for(int i=1; i < l; i++ ) {
			int j = parseInt(a[i]);
			int k = parseInt(b[i]);
			int d = j-k;
			if(d == 0) {
				continue;
			}
			return d;
		}
		return 0;
	}

	
	
}
