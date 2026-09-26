-- Phase 7.2: typed node resources. A node lists resources in the order the user chooses: a
-- tree (tree_id) or an external URL (url). Each has an optional label (the target's title
-- is shown when it's empty) and says whether it counts toward the node's readiness. Only
-- trees can count so far; a URL is always a reference. Later phases add more types, each
-- widening node_resources_resource_type_check.
--
-- Every existing link and linked tree moves here: a node's linked tree becomes its first
-- resource, counting, followed by its links, in their order, not counting. Then the old
-- columns and table go.

create table node_resources (
    node_id bigint not null,
    position integer not null,
    resource_type varchar(50) not null,
    tree_id bigint,
    url varchar(2048),
    label varchar(200),
    counts boolean not null,
    primary key (node_id, position),
    constraint node_resources_position_check check (position >= 0),
    constraint node_resources_resource_type_check check (resource_type in ('url', 'tree')),
    constraint node_resources_target_matches_type check (
        (resource_type = 'url' and url is not null and tree_id is null)
        or (resource_type = 'tree' and tree_id is not null and url is null)),
    constraint node_resources_url_never_counts check (resource_type <> 'url' or not counts),
    constraint node_resources_node_id_fk foreign key (node_id) references nodes (id),
    constraint node_resources_tree_id_fk foreign key (tree_id) references trees (id)
);

create index node_resources_tree_id_idx on node_resources (tree_id);

insert into node_resources (node_id, position, resource_type, tree_id, counts)
select id, 0, 'tree', linked_tree_id, true
from nodes
where linked_tree_id is not null;

insert into node_resources (node_id, position, resource_type, url, label, counts)
select l.node_id, l.position + case when n.linked_tree_id is null then 0 else 1 end, 'url', l.url, l.label, false
from node_links l
join nodes n on n.id = l.node_id;

drop table node_links;

alter table nodes drop constraint nodes_linked_tree_matches_source;
alter table nodes drop constraint nodes_readiness_source_type_check;
alter table nodes drop constraint nodes_linked_tree_id_fk;
drop index nodes_linked_tree_id_idx;
alter table nodes drop column linked_tree_id;
alter table nodes drop column readiness_source_type;
