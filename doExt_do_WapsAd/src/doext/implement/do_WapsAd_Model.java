package doext.implement;

import org.json.JSONObject;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import cn.waps.AppConnect;
import core.DoServiceContainer;
import core.helper.DoJsonHelper;
import core.interfaces.DoIScriptEngine;
import core.object.DoInvokeResult;
import doext.define.do_WapsAd_IMethod;
import doext.define.do_WapsAd_MAbstract;

/**
 * 自定义扩展MM组件Model实现，继承do_WapsAd_MAbstract抽象类，并实现do_WapsAd_IMethod接口方法；
 * #如何调用组件自定义事件？可以通过如下方法触发事件：
 * this.model.getEventCenter().fireEvent(_messageName, jsonResult);
 * 参数解释：@_messageName字符串事件名称，@jsonResult传递事件参数对象； 获取DoInvokeResult对象方式new
 * DoInvokeResult(this.getUniqueKey());
 */
public class do_WapsAd_Model extends do_WapsAd_MAbstract implements do_WapsAd_IMethod {

	private AppConnect appConnect;
	private LinearLayout adlayout;
	private Activity mContext;

	public static final int HEIGHT = 135;

	public do_WapsAd_Model() throws Exception {
		super();
		mContext = DoServiceContainer.getPageViewFactory().getAppContext();
		appConnect = AppConnect.getInstance(mContext);
		adlayout = new LinearLayout(mContext);
		FrameLayout.LayoutParams _lp = new FrameLayout.LayoutParams(-1, -2);
		mContext.addContentView(adlayout, _lp);
	}

	/**
	 * 同步方法，JS脚本调用该组件对象方法时会被调用，可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V），获取参数值使用API提供DoJsonHelper类；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public boolean invokeSyncMethod(String _methodName, JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		if ("show".equals(_methodName)) {
			show(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("close".equals(_methodName)) {
			close(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("getHeight".equals(_methodName)) {
			getHeight(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}

		return super.invokeSyncMethod(_methodName, _dictParas, _scriptEngine, _invokeResult);
	}

	/**
	 * 异步方法（通常都处理些耗时操作，避免UI线程阻塞），JS脚本调用该组件对象方法时会被调用， 可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V），获取参数值使用API提供DoJsonHelper类；
	 * @_scriptEngine 当前page JS上下文环境
	 * @_callbackFuncName 回调函数名 #如何执行异步方法回调？可以通过如下方法：
	 *                    _scriptEngine.callback(_callbackFuncName,
	 *                    _invokeResult);
	 *                    参数解释：@_callbackFuncName回调函数名，@_invokeResult传递回调函数参数对象；
	 *                    获取DoInvokeResult对象方式new
	 *                    DoInvokeResult(this.getUniqueKey());
	 */
	@Override
	public boolean invokeAsyncMethod(String _methodName, JSONObject _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) throws Exception {
		return super.invokeAsyncMethod(_methodName, _dictParas, _scriptEngine, _callbackFuncName);
	}

	/**
	 * 关闭广告；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void close(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		if (adlayout != null) {
			adlayout.setVisibility(View.GONE);
		}
		if (appConnect != null) {
			appConnect.close();
		}
	}

	/**
	 * 获取广告高度；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void getHeight(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		int _height = 0;
		if (adlayout != null) {
			_height = HEIGHT;
		}
		_invokeResult.setResultInteger(_height);
	}

	/**
	 * 显示广告；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void show(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		double _yZoom = _scriptEngine.getCurrentPage().getRootView().getYZoom();
		int _y = DoJsonHelper.getInt(_dictParas, "y", 0);
		_y = (int) (_yZoom * _y);
		appConnect.showBannerAd(mContext, adlayout);
		FrameLayout.LayoutParams _lp = (LayoutParams) adlayout.getLayoutParams();
		_lp.height = (int) (HEIGHT * _yZoom);
		_lp.topMargin = _y;
		adlayout.setVisibility(View.GONE);
		adlayout.setVisibility(View.VISIBLE);
	}
}