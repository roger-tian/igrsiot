﻿insert into igrs_user(id, user, password, role) values(1, 'admin', 'password', 'admin');
insert into igrs_room(id, room, name) values(1, '100', '100');
insert into igrs_room(id, room, name) values(2, '101', '101');
insert into igrs_device(id, type, dindex, name, ctype, cip, room) values(1, 'welcome', '0', '迎宾模式', '0', '', '100');
insert into igrs_device(id, type, dindex, name, ctype, cip, room) values(2, 'machine', '0', '前交互大屏', '0', '192.168.1.200', '100');
insert into igrs_device(id, type, dindex, name, ctype, cip, room) values(3, 'machine', '1', '后交互大屏', '0', '192.168.1.200', '100');
insert into igrs_device(id, type, dindex, name, ctype, cip, room) values(4, 'led', '0', '智能灯一', '0', '192.168.1.200', '100');
insert into igrs_device(id, type, dindex, name, ctype, cip, room) values(5, 'led', '1', '智能灯二', '0', '192.168.1.200', '100');
insert into igrs_device(id, type, dindex, name, ctype, cip, room) values(6, 'curtain', '0', '窗帘', '0', '192.168.1.200', '100');
insert into igrs_device(id, type, dindex, name, ctype, cip, room) values(7, 'purifier', '0', '净化器', '0', '', '100');
insert into igrs_device(id, type, dindex, name, ctype, cip, room) values(8, 'welcome', '0', '迎宾模式', '0', '', '101');
insert into igrs_device(id, type, dindex, name, ctype, cip, room) values(9, 'machine', '0', '前交互大屏', '0', '192.168.1.199', '101');
insert into igrs_device(id, type, dindex, name, ctype, cip, room) values(10, 'machine', '1', '后交互大屏', '0', '192.168.1.199', '101');
insert into igrs_device(id, type, dindex, name, ctype, cip, room) values(11, 'led', '0', '智能灯一', '0', '192.168.1.199', '101');
insert into igrs_device(id, type, dindex, name, ctype, cip, room) values(12, 'led', '1', '智能灯二', '0', '192.168.1.199', '101');
insert into igrs_device(id, type, dindex, name, ctype, cip, room) values(13, 'curtain', '0', '窗帘', '0', '192.168.1.199', '101');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(1, 1, 'switch', '0', '');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(2, 2, 'switch', '0', '10');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(3, 2, 'sig_source', '1', '12');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(4, 2, 'volume', '0', '11');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(5, 3, 'switch', '0', '50');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(6, 3, 'sig_source', '1', '12');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(7, 3, 'volume', '0', '11');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(8, 4, 'switch', '0', '20');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(9, 5, 'switch', '0', '21');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(10, 6, 'switch', '0', '60');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(11, 7, 'switch', '0', '');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(12, 8, 'switch', '0', '');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(13, 9, 'switch', '0', '10');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(14, 9, 'sig_source', '1', '12');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(15, 9, 'volume', '0', '11');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(16, 10, 'switch', '0', '50');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(17, 10, 'sig_source', '1', '12');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(18, 10, 'volume', '0', '11');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(19, 11, 'switch', '0', '20');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(20, 12, 'switch', '0', '21');
insert into igrs_device_status(id, device, attribute, value, cchannel) values(21, 13, 'switch', '0', '60');

