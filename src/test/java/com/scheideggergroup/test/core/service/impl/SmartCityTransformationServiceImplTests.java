package com.scheideggergroup.test.core.service.impl;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.scheideggergroup.core.model.Polyline;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.service.TransformationService;
import com.scheideggergroup.core.service.impl.SmartCityTransformationServiceImpl;

public class SmartCityTransformationServiceImplTests {

	@Test
	public void testMapFromPolylineToRoute() {
		
		String encPolylineToTest = "mpjyHx`i@VjAVKnAh@BHHX@LZR@Bj@Ml@WWc@]w@bAyAfBmCb@o@pLeQfCsDVa@@ODQR}AJ{A?{BGu" + 
        		"AD_@FKb@MTUX]Le@^kBVcAVo@Ta@|EaFh@m@FWaA{DCo@q@mCm@cC{A_GWeA}@sGSeAcA_EOSMa" + 
        		"@}A_GsAwFkAiEoAaFaBoEGo@]_AIWW{AQyAUyBQqAI_BFkEd@aHZcDlAyJLaBPqDDeD?mBEiA}@F]yKWqGSkI" + 
        		"CmCIeZIuZi@_Sw@{WgAoXS{DOcAWq@KQGIFQDGn@Y`@MJEFIHyAVQVOJGHgFRJBBCCSKBcAKoACyA?m@^y" + 
        		"VJmLJ{FGGWq@e@eBIe@Ei@?q@Bk@Hs@Le@Rk@gCuIkJcZsDwLd@g@Oe@o@mB{BgHQYq@qBQYOMSM" + 
        		"GBUBGCYc@E_@H]DWJST?JFFHBDNBJ?LED?LBv@WfAc@@EDGNK|@e@hAa@`Bk@b@OEk@Go@IeACoA@" + 
        		"a@PyB`@yDDc@e@K{Bi@oA_@w@]m@_@]QkBoAwC{BmAeAo@s@uAoB_AaBmAwCa@mAo@iCgAwFg@iD" + 
        		"q@}G[uEU_GBuP@cICmA?eI?qCB{FBkCI}BOyCMiAGcAC{AN{YFqD^}FR}CNu@JcAHu@b@_E`@}DVsB^mBTsAQ" + 
        		"KkCmAg@[YQOIOvAi@[m@e@s@g@GKCKAEJIn@g@GYGIc@ScBoAf@{A`@uAlBfAG`@";
		
		Polyline polylineToTest = new Polyline();
		polylineToTest.setEncodedPolyline(encPolylineToTest);
		TransformationService polyServ = new SmartCityTransformationServiceImpl();
		Route r = polyServ.map(polylineToTest);
		int coordQuantity = r.getPath().size();
		
		Assert.assertTrue("Route should contain more than one coordinate.", coordQuantity > 1);
	}
}
