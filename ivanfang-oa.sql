create database `ivanfang-oa`;

use `ivanfang-oa`;

create table `sys_role` (
	`id` bigint(20) not null auto_increment comment "角色id",
    `role_name` varchar(20) not null default "" comment "角色名稱",
    `role_code` varchar(20) default null comment "角色編碼",
    `description` varchar(255) default null comment "描述",
    `create_time` timestamp not null default current_timestamp comment "創建時間",
    `update_time` timestamp not null default current_timestamp on update current_timestamp comment "更新時間",
    `is_deleted` tinyint(3) not null default '0' comment '刪除標記（0:不可用 1:可用）',
    primary key (`id`)
) auto_increment=9 default charset=utf8 comment="角色";

CREATE TABLE `sys_user` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '使用者id',
	`username` varchar(20) NOT NULL DEFAULT '' COMMENT '使用者名稱',
	`password` varchar(32) NOT NULL DEFAULT '' COMMENT '密碼',
	`name` varchar(50) DEFAULT NULL COMMENT '真實姓名',
	`phone` varchar(11) DEFAULT NULL COMMENT '手機號碼',
	`head_url` varchar(200) DEFAULT NULL COMMENT '頭像地址',
	`dept_id` bigint(20) DEFAULT NULL COMMENT '部門id',
	`post_id` bigint(20) DEFAULT NULL COMMENT '職位id',
	`open_id` varchar(255) DEFAULT NULL COMMENT '微信openId',
	`description` varchar(255) DEFAULT NULL COMMENT '描述',
	`status` tinyint(3) DEFAULT NULL COMMENT '狀態(1:正常 0:停用)',
	`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
	`is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '刪除標記(0:不可用 1:可用)',
	PRIMARY KEY (`id`),
	UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='使用者';

INSERT INTO `sys_user` VALUES 
	(1,'admin','0192023a7bbd73250516f069df18b500','Admin The Best',NULL,'https://cdn-icons-png.flaticon.com/512/2206/2206368.png',NULL,NULL,NULL,'後臺管理員',1,'2023-10-14 16:43:26','2023-10-15 11:03:51',0),
	(13,'Amy','c1ba18572a90fe2bf2db58fd728fd349','Amy Dubanowski','0909123123','https://img.nbc.com/files/images/2019/9/05/amy.jpg',1,17,NULL,'Amy 有一個女兒',1,'2023-10-13 10:50:09','2023-10-14 19:17:14',0),
	(14,'Glenn','ce399ae331f628ec27707b8720e0f6f8','Glenn Phillip Sturgis','0909123666','https://img.nbc.com/files/images/2019/9/05/glenn.jpg',0,11,'','Glenn 人很好',1,'2023-10-13 15:23:17','2023-10-14 19:17:14',0),
	(15,'Jonah','b352b2758bfec542b67d58d77ed23cb7','Jonah Simms','0909888777','https://img.nbc.com/sites/nbcunbc/files/images/2019/9/05/jonah.jpg',NULL,NULL,NULL,NULL,1,'2023-10-13 16:20:24','2023-10-14 19:14:54',0),
	(16,'Cheyenne','cb41b25d061349a059f10942828e587b','Cheyenne Tyler Lee','0909111222','https://img.nbc.com/files/images/2019/9/05/cheyenne.jpg',NULL,NULL,NULL,NULL,1,'2023-10-13 16:21:07','2023-10-14 19:14:54',0),
	(17,'Mateo','4cdda253aa0bac1bcddae7348e4fb881','Liwanag','0909000999',NULL,NULL,NULL,NULL,NULL,1,'2023-10-13 16:21:59','2023-10-14 19:12:08',0),
	(18,'Dina','f093c0fed979519fbc43d772b76f5c86','Dina Fox','0909123123',NULL,NULL,NULL,NULL,NULL,1,'2023-10-13 16:22:55','2023-10-14 19:12:08',1),
	(20,'Jeff','dc2af307c55523ce42701dbe43880d35','Jeff Sutton','0987987987',NULL,NULL,NULL,NULL,NULL,1,'2023-10-21 14:09:25','2023-10-21 14:09:25',0);


CREATE TABLE `sys_user_role` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主鍵id',
	`role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色id',
	`user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '使用者id',
	`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
	`is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '刪除標記(0:不可用1:可用)',
	PRIMARY KEY (`id`),
	KEY `idx_role_id` (`role_id`),
	KEY `idx_admin_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='使用者角色';

CREATE TABLE `sys_menu` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '編號',
	`parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所屬上級',
	`name` varchar(20) NOT NULL DEFAULT '' COMMENT '名稱',
	`type` tinyint(3) NOT NULL DEFAULT '0' COMMENT '類型(0:目錄,1:選單,2:按鈕)',
	`path` varchar(100) DEFAULT NULL COMMENT '路由地址',
	`component` varchar(100) DEFAULT NULL COMMENT '元件路徑',
	`perms` varchar(100) DEFAULT NULL COMMENT '許可權標識',
	`icon` varchar(100) DEFAULT NULL COMMENT '圖示',
	`sort_value` int(11) DEFAULT NULL COMMENT '排序',
	`status` tinyint(4) DEFAULT NULL COMMENT '狀態(0:禁止, 1:正常)',
	`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
	`is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '刪除標記(0:不可用 1:可用)',
	PRIMARY KEY (`id`),
	KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COMMENT='選單表';

INSERT INTO `sys_menu` VALUES 
	(2,0,'系統管理',0,'system','Layout',NULL,'el-icon-s-tools',1,1,'2021-05-31 18:05:37','2022-06-09 09:23:24',0),
	(3,2,'使用者管理',1,'sysUser','system/sysUser/list','','el-icon-s-custom',1,1,'2021-05-31 18:05:37','2023-10-21 13:51:23',0),
	(4,2,'角色管理',1,'sysRole','system/sysRole/list','','el-icon-user-solid',2,1,'2021-05-31 18:05:37','2022-06-09 09:37:18',0),
	(5,2,'選單管理',1,'sysMenu','system/sysMenu/list','','el-icon-s-unfold',3,1,'2021-05-31 18:05:37','2023-10-21 13:51:23',0),
	(6,3,'查看',2,NULL,NULL,'btn.sysUser.list',NULL,1,1,'2021-05-31 18:05:37','2022-06-09 09:22:38',0),
	(7,3,'新增',2,NULL,NULL,'btn.sysUser.add',NULL,1,1,'2021-05-31 18:05:37','2022-06-09 09:22:38',0),
	(8,3,'修改',2,NULL,NULL,'btn.sysUser.update',NULL,1,1,'2021-05-31 18:05:37','2022-06-09 09:22:38',0),
	(9,3,'刪除',2,NULL,NULL,'btn.sysUser.remove',NULL,1,1,'2021-05-31 18:05:37','2022-06-09 09:22:38',0),
	(10,4,'查看',2,NULL,NULL,'btn.sysRole.list',NULL,1,1,'2021-05-31 18:05:37','2022-06-09 09:22:38',0),
	(11,4,'新增',2,NULL,NULL,'btn.sysRole.add',NULL,1,1,'2021-05-31 18:05:37','2022-06-09 09:22:38',0),
	(12,4,'修改',2,NULL,NULL,'btn.sysRole.update',NULL,1,1,'2021-05-31 18:05:37','2022-06-09 09:22:38',0),
	(13,4,'刪除',2,NULL,NULL,'btn.sysRole.remove',NULL,1,1,'2021-05-31 18:05:37','2022-06-09 09:22:38',0),
	(14,5,'查看',2,NULL,NULL,'btn.sysMenu.list',NULL,1,1,'2021-05-31 18:05:37','2022-06-09 09:22:38',0),
	(15,5,'新增',2,NULL,NULL,'btn.sysMenu.add',NULL,1,1,'2021-05-31 18:05:37','2022-06-09 09:22:38',0),
	(16,5,'修改',2,NULL,NULL,'btn.sysMenu.update',NULL,1,1,'2021-05-31 18:05:37','2022-06-09 09:22:38',0),
	(17,5,'刪除',2,NULL,NULL,'btn.sysMenu.remove',NULL,1,1,'2021-05-31 18:05:37','2022-06-09 09:22:38',0),
	(18,3,'分配角色',2,NULL,NULL,'btn.sysUser.assignRole',NULL,1,1,'2022-05-23 17:14:32','2022-06-09 09:22:38',0),
	(19,4,'分配許可權',2,'assignAuth','system/sysRole/assignAuth','btn.sysRole.assignAuth',NULL,1,1,'2022-05-23 17:18:14','2022-06-09 09:22:38',0),
	(20,2,'部門管理',1,'sysDept','system/sysDept/list','','el-icon-s-operation',4,1,'2022-05-24 10:07:05','2023-10-26 16:35:45',1),
	(21,20,'查看',2,NULL,NULL,'btn.sysDept.list',NULL,1,1,'2022-05-24 10:07:44','2022-06-09 09:22:38',0),
	(22,2,'職位管理',1,'sysPost','system/sysPost/list','','el-icon-more-outline',5,1,'2022-05-24 10:25:30','2023-10-26 16:35:45',1),
	(23,22,'查看',2,NULL,NULL,'btn.sysPost.list',NULL,1,1,'2022-05-24 10:25:45','2022-06-09 09:22:38',0),
	(24,20,'新增',2,NULL,NULL,'btn.sysDept.add',NULL,1,1,'2022-05-25 15:31:27','2022-06-09 09:22:38',0),
	(25,20,'修改',2,NULL,NULL,'btn.sysDept.update',NULL,1,1,'2022-05-25 15:31:41','2022-06-09 09:22:38',0),
	(26,20,'刪除',2,NULL,NULL,'btn.sysDept.remove',NULL,1,1,'2022-05-25 15:31:59','2022-06-09 09:22:38',0),
	(27,22,'新增',2,NULL,NULL,'btn.sysPost.add',NULL,1,1,'2022-05-25 15:32:44','2022-06-09 09:22:38',0),
	(28,22,'修改',2,NULL,NULL,'btn.sysPost.update',NULL,1,1,'2022-05-25 15:32:58','2022-06-09 09:22:38',0),
	(29,22,'刪除',2,NULL,NULL,'btn.sysPost.remove',NULL,1,1,'2022-05-25 15:33:11','2022-06-09 09:22:38',0),
	(30,34,'操作日誌',1,'sysOperLog','system/sysOperLog/list','','el-icon-document-remove',7,1,'2022-05-26 16:09:59','2023-10-26 16:36:49',1),
	(31,30,'查看',2,NULL,NULL,'btn.sysOperLog.list',NULL,1,1,'2022-05-26 16:10:17','2022-06-09 09:22:38',0),
	(32,34,'登錄日誌',1,'sysLoginLog','system/sysLoginLog/list','','el-icon-s-goods',8,1,'2022-05-26 16:36:13','2023-10-26 16:36:49',1),
	(33,32,'查看',2,NULL,NULL,'btn.sysLoginLog.list',NULL,1,1,'2022-05-26 16:36:31','2022-06-09 09:36:36',0),
	(34,2,'日誌管理',0,'log','ParentView','','el-icon-tickets',6,1,'2022-05-31 13:23:07','2023-10-26 16:36:49',1),
	(35,0,'審批設置',0,'processSet','Layout','','el-icon-setting',1,1,'2022-12-01 09:32:46','2022-12-01 09:32:46',0),
	(36,35,'審批模板',1,'processTemplate','processSet/processTemplate/list','','el-icon-s-help',2,1,'2022-12-01 09:37:08','2023-10-20 11:38:31',0),
	(37,36,'查看',2,'','','btn.processTemplate.list','',1,1,'2022-12-01 09:37:49','2022-12-01 09:37:49',0),
	(38,36,'審批模板設置',2,'templateSet','processSet/processTemplate/templateSet','btn.processTemplate.templateSet','',1,1,'2022-12-01 14:52:08','2023-10-20 16:13:07',0),
	(39,35,'審批類型',1,'processType','processSet/processType/list','','el-icon-s-unfold',1,1,'2022-12-02 14:46:18','2023-10-20 16:12:00',0),
	(40,39,'查看',2,'','','btn.processType.list','',1,1,'2022-12-02 14:46:41','2022-12-02 14:46:41',0),
	(41,0,'審批管理',0,'processMgr','Layout','','el-icon-more-outline',1,1,'2022-12-02 14:48:11','2022-12-20 09:29:30',0),
	(42,41,'審批列表',1,'process','processMgr/process/list','','el-icon-document-remove',1,1,'2022-12-02 14:49:06','2022-12-02 14:59:17',0),
	(43,42,'查看',2,'','','btn.process.list','',1,1,'2022-12-02 14:49:24','2022-12-02 14:49:24',0),
	(44,36,'線上流程設置',2,'onlineProcessSet','processSet/processTemplate/onlineProcessSet','btn.processTemplate.onlineProcessSet','',1,1,'2022-12-08 10:13:15','2022-12-19 18:57:35',0),
	(45,39,'新增',2,'','','btn.processType.add','',1,1,'2022-12-09 09:14:53','2022-12-09 09:14:53',0),
	(46,39,'修改',2,'','','btn.processType.update','',1,1,'2022-12-09 09:15:10','2022-12-09 09:15:10',0),
	(47,39,'刪除',2,'','','btn.processType.remove','',1,1,'2022-12-09 09:15:25','2022-12-09 09:15:25',0),
	(48,36,'刪除',2,'','','btn.processTemplate.remove','',1,1,'2022-12-09 09:22:29','2022-12-09 09:22:29',0),
	(49,36,'發佈',2,'','','btn.processTemplate.publish','',1,1,'2022-12-09 09:24:47','2022-12-09 09:24:47',0),
	(50,0,'公眾號選單',0,'wechat','Layout','','el-icon-s-operation',1,1,'2022-12-13 09:06:58','2023-10-26 16:37:31',1),
	(51,50,'選單列表',1,'menu','wechat/menu/list','','el-icon-s-help',1,1,'2022-12-13 09:07:52','2023-10-26 16:37:31',1),
	(52,51,'查看',2,'','','btn.menu.list','',1,1,'2022-12-13 09:08:48','2022-12-13 17:58:23',0),
	(53,51,'新增',2,'','','btn.menu.add','',1,1,'2022-12-13 16:29:25','2022-12-13 17:58:34',0),
	(54,51,'修改',2,'','','btn.menu.update','',1,1,'2022-12-13 16:29:41','2022-12-13 17:58:42',0),
	(55,51,'刪除',2,'','','btn.menu.remove','',1,1,'2022-12-13 16:29:59','2022-12-13 17:58:47',0),
	(56,51,'刪除微信選單',2,'','','btn.menu.removeMenu','',1,1,'2022-12-13 16:30:36','2022-12-13 17:58:54',0),
	(57,51,'同步微信選單',2,'','','btn.menu.syncMenu','',1,1,'2022-12-13 16:31:00','2022-12-13 17:59:01',0);

CREATE TABLE `sys_role_menu` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`role_id` bigint(20) NOT NULL DEFAULT '0',
	`menu_id` bigint(11) NOT NULL DEFAULT '0',
	`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
	`is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '刪除標記：0不可用、1：可用)',
	PRIMARY KEY (`id`),
	KEY `idx_role_id` (`role_id`),
	KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='角色選單表';

CREATE TABLE `process_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '類型名稱',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '刪除標記（0:不可用 1:可用）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='審批類型';

CREATE TABLE `process_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '範本名稱',
  `icon_url` varchar(100) DEFAULT NULL COMMENT '圖示路徑',
  `process_type_id` varchar(255) DEFAULT NULL,
  `form_props` text COMMENT '表單屬性',
  `form_options` text COMMENT '表單選項',
  `process_definition_key` varchar(20) DEFAULT NULL COMMENT '流程定義key',
  `process_definition_path` varchar(255) DEFAULT NULL COMMENT '流程定義上傳路徑',
  `process_model_id` varchar(255) DEFAULT NULL COMMENT '流程定義模型id',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` tinyint(3) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '刪除標記（0:不可用 1:可用）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE `process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `process_code` varchar(50) NOT NULL DEFAULT '' COMMENT '審批code',
  `user_id` bigint(1) NOT NULL DEFAULT '0' COMMENT '使用者id',
  `process_template_id` bigint(20) DEFAULT NULL COMMENT '審批模板id',
  `process_type_id` bigint(20) DEFAULT NULL COMMENT '審批類型id',
  `title` varchar(255) DEFAULT NULL COMMENT '標題',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `form_values` text COMMENT '表單值',
  `process_instance_id` varchar(255) DEFAULT NULL COMMENT '流程實例id',
  `current_auditor` varchar(255) DEFAULT NULL COMMENT '目前審批人',
  `status` tinyint(3) DEFAULT NULL COMMENT '狀態（0：預設 1：審批中 2：審批通過 -1：駁回）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '刪除標記（0:不可用 1:可用）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='審批類型';

CREATE TABLE `process_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `process_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '審批流程id',
  `description` varchar(255) DEFAULT NULL COMMENT '審批描述',
  `status` tinyint(3) DEFAULT '0' COMMENT '狀態',
  `operate_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作使用者id',
  `operate_user` varchar(20) DEFAULT NULL COMMENT '操作使用者',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '刪除標記（0:不可用 1:可用）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='審批記錄';
