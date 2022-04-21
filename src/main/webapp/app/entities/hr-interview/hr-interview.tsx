import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import {Button, Input,  Table} from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './hr-interview.reducer';
import { IHrInterview } from 'app/shared/model/hr-interview.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import {getEntities as getProcesses} from "app/entities/process/process.reducer";


export const HrInterview = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const hrInterviewList = useAppSelector(state => state.hrInterview.entities);
  const loading = useAppSelector(state => state.hrInterview.loading);
  const processes = useAppSelector(state => state.process.entities);
  const [filter, setFilter] = useState('');

  useEffect(() => {
    dispatch(getEntities({}));
    dispatch(getProcesses({}));

  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;
  const changeFilter = evt => setFilter(evt.target.value);

  const filterFn = l => l.editBy.toString().toUpperCase().includes(filter.toUpperCase());

  return (
    <div>
      <h2 id="hr-interview-heading" data-cy="HrInterviewHeading">
        <Translate contentKey="iKaMetApp.hrInterview.home.title">Hr Interviews</Translate>

        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="iKaMetApp.hrInterview.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="iKaMetApp.hrInterview.home.createLabel">Create new Hr Interview</Translate>
          </Link>

        </div>
        <div>
          <Input placeholder={"Search HrInterviews Name"} value={filter}  style={{width :'1000px'}} onChange={changeFilter}  type="search"  name="search" id="search"   />
        </div>


      </h2>
      <div className="table-responsive">
        {hrInterviewList && hrInterviewList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="iKaMetApp.hrInterview.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.hrInterview.process">Process</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.hrInterview.score">Score</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.hrInterview.ikStatus">Ik Status</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.hrInterview.notes">Notes</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.hrInterview.editBy">Edit By</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.hrInterview.date">Date</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {hrInterviewList.filter(filterFn ).map((hrInterview, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${hrInterview.id}`} color="link" size="sm">
                      {hrInterview.id}
                    </Button>
                  </td>
                  <td>
                  {processes
                    ? processes.filter(ide =>ide.id === hrInterview.process.id).map(otherEntity => (
                      <option value={hrInterview.process.id} key={hrInterview.process.id}>
                        {otherEntity.candidate.firstName}{otherEntity.candidate.lastName}{hrInterview.process.id}
                      </option>
                    ))
                    : null}
                  </td>
                  <td>{hrInterview.score}</td>
                  <td>{hrInterview.ikStatus}</td>
                  <td>{hrInterview.notes}</td>
                  <td>{hrInterview.editBy}</td>
                  <td>{hrInterview.date ? <TextFormat type="date" value={hrInterview.date} format={APP_DATE_FORMAT} /> : null}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${hrInterview.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${hrInterview.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${hrInterview.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="iKaMetApp.hrInterview.home.notFound">No Hr Interviews found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default HrInterview;
