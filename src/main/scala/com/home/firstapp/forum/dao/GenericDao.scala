package com.home.firstapp.forum.dao

/**
 *
 * @author Antanas Bastys
 */
trait GenericDao[UNPERSITED, PERSISTED <: UNPERSITED, ID] {
  def findAll(): List[PERSISTED]
  def find(id: ID): PERSISTED
  def delete(id: ID): Unit
  def persist(pbj:UNPERSITED): PERSISTED
  def update(newer: PERSISTED): PERSISTED
}
