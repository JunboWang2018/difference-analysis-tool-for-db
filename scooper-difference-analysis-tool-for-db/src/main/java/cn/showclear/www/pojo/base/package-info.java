/**
 * 
 * base下建业务包
 * 
 * base整体结构如下：
 * 	-base
 * 		-{业务1}
 * 			-XxxDo：从数据库查询的简单结果对象，一直传输到rest层
 *   		-XxxQo：从rest层一直传输到ds层做数据查询的对象
 *   		-XxxTo：数据传输对象，单个或多个Do组合而成，可以提供给其他项目使用（按需使用）
 * 		-{业务名2}
 */
package cn.showclear.www.pojo.base;