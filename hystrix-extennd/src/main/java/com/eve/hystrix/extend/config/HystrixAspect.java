package com.eve.hystrix.extend.config;

import com.eve.hystrix.extend.RequestMappingInfo;
import com.eve.hystrix.extend.XHystrixCommand;
import com.eve.hystrix.extend.core.CommandListener;
import com.eve.hystrix.extend.core.HystrixFallback;
import com.google.common.base.Throwables;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;


/**
 *
 * @author xie
 */
@Aspect
public class HystrixAspect {


	@Autowired(required = false)
	private CommandListener listener;


	@Autowired(required = false)
	private HystrixFallback hystrixFallback;

	/**
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)||@annotation(com.eve.hystrix.extend.core.XCommand)")
	public Object methodsAnnotatedWithHystrixCommand(final ProceedingJoinPoint joinPoint) throws Throwable {

		Method method = getMethodFromTarget(joinPoint);
		RequestMappingInfo requestMappingInfo = RequestMappingInfo.methodMappings.get(method);
		if(requestMappingInfo == null){
			return joinPoint.proceed();
		}
		return new XHystrixCommand(requestMappingInfo, joinPoint.getTarget(), joinPoint.getArgs(), hystrixFallback, listener).execute();
	}

	public static Method getMethodFromTarget(JoinPoint joinPoint) {
		Method method = null;
		if (joinPoint.getSignature() instanceof MethodSignature) {
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			method = getDeclaredMethod(joinPoint.getTarget().getClass(), signature.getName(),
				getParameterTypes(joinPoint));
		}
		return method;
	}

	/**
	 * Gets parameter types of the join point.
	 *
	 * @param joinPoint the join point
	 * @return the parameter types for the method this object
	 *         represents
	 */
	public static Class[] getParameterTypes(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		return method.getParameterTypes();
	}

	/**
	 * Gets declared method from specified type by mame and parameters types.
	 *
	 * @param type           the type
	 * @param methodName     the name of the method
	 * @param parameterTypes the parameter array
	 * @return a {@link Method} object or null if method doesn't exist
	 */
	public static Method getDeclaredMethod(Class<?> type, String methodName, Class<?>... parameterTypes) {
		Method method = null;
		try {
			method = type.getDeclaredMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException e) {
			Class<?> superclass = type.getSuperclass();
			if (superclass != null) {
				method = getDeclaredMethod(superclass, methodName, parameterTypes);
			}
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return method;
	}

}
