CREATE TABLE `sys_execute_log` (
  `id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '主键',
  `source_instance_id` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '调用放的实例 id',
  `des_item_id` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '请求的方法的 id',
  `updated_by` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `content` text COLLATE utf8_bin COMMENT '日志内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统日志表';