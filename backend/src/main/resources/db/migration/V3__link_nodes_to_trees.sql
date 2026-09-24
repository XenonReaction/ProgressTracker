-- Phase 5.2: a node can take its readiness from a whole tree instead of a hand-entered value.
-- linked_tree_id is set exactly when readiness_source_type is 'linked_tree'. The
-- hand-entered readiness stays in its column while a node is linked, so unlinking restores it.

alter table nodes add column linked_tree_id bigint;

alter table nodes add constraint nodes_linked_tree_id_fk
    foreign key (linked_tree_id) references trees (id);

create index nodes_linked_tree_id_idx on nodes (linked_tree_id);

alter table nodes drop constraint nodes_readiness_source_type_check;
alter table nodes add constraint nodes_readiness_source_type_check
    check (readiness_source_type in ('manual', 'linked_tree'));

alter table nodes add constraint nodes_linked_tree_matches_source
    check ((readiness_source_type = 'linked_tree') = (linked_tree_id is not null));
