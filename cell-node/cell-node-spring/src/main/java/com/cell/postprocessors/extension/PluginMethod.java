package com.cell.postprocessors.extension;

import com.cell.bridge.ExtensionClass;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.core.type.MethodMetadata;

public class PluginMethod {
	protected final MethodMetadata metadata;

	protected final ExtensionClass configurationClass;


	public PluginMethod(MethodMetadata metadata, ExtensionClass configurationClass) {
		this.metadata = metadata;
		this.configurationClass = configurationClass;
	}


	public MethodMetadata getMetadata() {
		return this.metadata;
	}

	public ExtensionClass getExtensionClass() {
		return this.configurationClass;
	}

	public Location getResourceLocation() {
		return new Location(this.configurationClass.getResource(), this.metadata);
	}

	String getFullyQualifiedMethodName() {
		return this.metadata.getDeclaringClassName() + "#" + this.metadata.getMethodName();
	}

	static String getShortMethodName(String fullyQualifiedMethodName) {
		return fullyQualifiedMethodName.substring(fullyQualifiedMethodName.indexOf('#') + 1);
	}

	public void validate(ProblemReporter problemReporter) {
		if (getMetadata().isStatic()) {
			// static @Bean methods have no constraints to validate -> return immediately
			return;
		}
	}


	@Override
	public String toString() {
		return String.format("[%s:name=%s,declaringClass=%s]",
				getClass().getSimpleName(), getMetadata().getMethodName(), getMetadata().getDeclaringClassName());
	}

}
