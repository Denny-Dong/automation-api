package io.creams.test.api.runner;

import io.creams.test.api.web.Comment.Comment_Steps;
import io.creams.test.api.web.analysis.Analysis_Steps;
import io.creams.test.api.web.buildings.Buildings_Steps;
import io.creams.test.api.web.businessCommon.BusinessCommon_Steps;
import io.creams.test.api.web.contractsv2.Contract_Steps;
import io.creams.test.api.web.export.Export_Steps;
import io.creams.test.api.web.marketing.Marketing_Steps;
import io.creams.test.api.web.oa.OA_Steps;
import io.creams.test.api.web.propertyContracts.PropertyContracts_Steps;
import io.creams.test.api.web.roomController.roomController_Steps;
import io.creams.test.api.web.template.Template_Steps;
import io.creams.test.api.web.tenant.Tenant_Steps;
import io.creams.test.api.web.user.Users_Steps;
import io.creams.test.api.web.workFlow.WorkFlow_Steps;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ Buildings_Steps.class, Users_Steps.class, BusinessCommon_Steps.class, Marketing_Steps.class,
		OA_Steps.class, PropertyContracts_Steps.class, Tenant_Steps.class, roomController_Steps.class,
		Contract_Steps.class, Analysis_Steps.class, Template_Steps.class, Export_Steps.class,Comment_Steps.class,WorkFlow_Steps.class})
public class WebRunner {

}
