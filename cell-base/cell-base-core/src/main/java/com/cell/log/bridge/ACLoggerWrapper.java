package com.cell.log.bridge;

import com.cell.log.LOG;
import com.cell.log.LogLevel;
import com.cell.log.LogTypeEnums;
import com.cell.models.Module;
import org.apache.commons.logging.Log;

public class ACLoggerWrapper implements Log
{
	private Module module;

	public ACLoggerWrapper(Module module) {
		this.module = module;
	}

	@Override
	public void debug(Object message) {
		LOG.debug(module, LogTypeEnums.THIRD_PARTY, "{}", message);
	}

	@Override
	public void debug(Object message, Throwable t) {
		LOG.debug(module, t, LogTypeEnums.THIRD_PARTY, "{}", message);
	}

	@Override
	public void error(Object message) {
		LOG.error(module, null, LogTypeEnums.THIRD_PARTY, "{}", message);
	}

	@Override
	public void error(Object message, Throwable t) {
		LOG.error(module, t, LogTypeEnums.THIRD_PARTY, "{}", message);
	}

	@Override
	public void fatal(Object message) {
		LOG.error(module, null, LogTypeEnums.THIRD_PARTY, "{}", message);
	}

	@Override
	public void fatal(Object message, Throwable t) {
		LOG.error(module, t, LogTypeEnums.THIRD_PARTY, "{}", message);
	}

	@Override
	public void info(Object message) {
		LOG.info(module, LogTypeEnums.THIRD_PARTY, "{}", message);
	}

	@Override
	public void info(Object message, Throwable t) {
		LOG.info(module, t, LogTypeEnums.THIRD_PARTY, "{}", message);
	}

	@Override
	public boolean isDebugEnabled() {
		return LOG.haveReceiver(module, LogLevel.DEBUG, LogTypeEnums.THIRD_PARTY.getValue());
	}

	@Override
	public boolean isErrorEnabled() {
		return LOG.haveReceiver(module, LogLevel.ERROR, LogTypeEnums.THIRD_PARTY.getValue());
	}

	@Override
	public boolean isFatalEnabled() {
		return LOG.haveReceiver(module, LogLevel.ERROR, LogTypeEnums.THIRD_PARTY.getValue());
	}

	@Override
	public boolean isInfoEnabled() {
		return LOG.haveReceiver(module, LogLevel.INFO, LogTypeEnums.THIRD_PARTY.getValue());
	}

	@Override
	public boolean isTraceEnabled() {
		return LOG.haveReceiver(module, LogLevel.TRACE, LogTypeEnums.THIRD_PARTY.getValue());
	}

	@Override
	public boolean isWarnEnabled() {
		return LOG.haveReceiver(module, LogLevel.WARN, LogTypeEnums.THIRD_PARTY.getValue());
	}

	@Override
	public void trace(Object message) {
		LOG.trace(module, LogTypeEnums.THIRD_PARTY, "{}", message);
	}

	@Override
	public void trace(Object message, Throwable t) {
		LOG.trace(module, t, LogTypeEnums.THIRD_PARTY, "{}", message);
	}

	@Override
	public void warn(Object message) {
		LOG.warn(module, LogTypeEnums.THIRD_PARTY, "{}", message);
	}

	@Override
	public void warn(Object message, Throwable t) {
		LOG.warn(module, t, LogTypeEnums.THIRD_PARTY, "{}", message);
	}

}
