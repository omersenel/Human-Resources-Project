import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import {Button,  Input,  Table} from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import {getEntities, getEntity} from './technical-interview.reducer';
import { ITechnicalInterview } from 'app/shared/model/technical-interview.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import {getEntities as getProcesses} from "app/entities/process/process.reducer";

export const TechnicalInterview = (props: RouteComponentProps<{ url: string  }>) => {

  const dispatch = useAppDispatch();
  const technicalInterviewList = useAppSelector(state => state.technicalInterview.entities);
  const loading = useAppSelector(state => state.technicalInterview.loading);
  const processes = useAppSelector(state => state.process.entities);
  const [filter, setFilter] = useState('');
  useEffect(() => {
    dispatch(getEntities({}));
    dispatch(getProcesses({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };
  const changeFilter = evt => setFilter(evt.target.value);

  const filterFn = l => l.editBy.toString().toUpperCase().includes(filter.toUpperCase());
  const { match } = props;

  return (
    <div>
      <h2 id="technical-interview-heading" data-cy="TechnicalInterviewHeading">
        <Translate contentKey="iKaMetApp.technicalInterview.home.title">Technical Interviews</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="iKaMetApp.technicalInterview.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="iKaMetApp.technicalInterview.home.createLabel">Create new Technical Interview</Translate>
          </Link>
        </div>
        <div>
          <Input placeholder={"Search HrInterviews Name"} value={filter}  style={{width :'1000px'}} onChange={changeFilter}  type="search"  name="search" id="search"   />
        </div>
      </h2>
      <div className="table-responsive">
        {technicalInterviewList && technicalInterviewList.length > 0 ? (
          <Table responsive>
            <thead>
            <tr>
              <th>
                <Translate contentKey="iKaMetApp.technicalInterview.id">ID</Translate>
              </th>
              <th>
                <Translate contentKey="iKaMetApp.technicalInterview.process">Process</Translate>
              </th>

              <th>
                <Translate contentKey="iKaMetApp.technicalInterview.score">Score</Translate>
              </th>
              <th>
                <Translate contentKey="iKaMetApp.technicalInterview.technicalStatus">Technical Status</Translate>
              </th>
              <th>
                <Translate contentKey="iKaMetApp.technicalInterview.notes">Notes</Translate>
              </th>
              <th>
                <Translate contentKey="iKaMetApp.technicalInterview.editBy">Edit By</Translate>
              </th>
              <th>
                <Translate contentKey="iKaMetApp.technicalInterview.date">Date</Translate>
              </th>
              <th />
            </tr>
            </thead>
            <tbody>
            {technicalInterviewList.filter(filterFn ).map((technicalInterview, i) => (
              <tr key={`entity-${i}`} data-cy="entityTable">
                <td>
                  <Button tag={Link} to={`${match.url}/${technicalInterview.id}`} color="link" size="sm">
                    {technicalInterview.id}
                  </Button>
                </td>
                <td>
                  {processes
                    ? processes.filter(ide =>ide.id === technicalInterview.process.id).map(otherEntity => (

                      <option value={technicalInterview.process.id} key={technicalInterview.process.id}>

                        {otherEntity.candidate.firstName}{otherEntity.candidate.lastName}{technicalInterview.process.id}
                      </option>
                    ))
                    : null}
                </td>

                <td>{technicalInterview.score}</td>
                <td>{technicalInterview.technicalStatus}</td>
                <td>{technicalInterview.notes}</td>
                <td>{technicalInterview.editBy}</td>
                <td>
                  {technicalInterview.date ? <TextFormat type="date" value={technicalInterview.date} format={APP_DATE_FORMAT} /> : null}
                </td>

                <td className="text-right">
                  <div className="btn-group flex-btn-group-container">
                    <Button tag={Link} to={`${match.url}/${technicalInterview.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                      <FontAwesomeIcon icon="eye" />{' '}
                      <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                    </Button>
                    <Button
                      tag={Link}
                      to={`${match.url}/${technicalInterview.id}/edit`}
                      color="primary"
                      size="sm"
                      data-cy="entityEditButton"
                    >
                      <FontAwesomeIcon icon="pencil-alt" />{' '}
                      <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                    </Button>
                    <Button
                      tag={Link}
                      to={`${match.url}/${technicalInterview.id}/delete`}
                      color="danger"
                      size="sm"
                      data-cy="entityDeleteButton"
                    >
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
              <Translate contentKey="iKaMetApp.technicalInterview.home.notFound">No Technical Interviews found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default TechnicalInterview;
